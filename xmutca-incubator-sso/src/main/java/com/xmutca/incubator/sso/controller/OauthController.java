package com.xmutca.incubator.sso.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.xmutca.incubator.core.common.response.Receipt;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.sso.config.SystemProperties;
import com.xmutca.incubator.sso.dto.TokenResponseDto;
import com.xmutca.incubator.sso.model.OauthClientInfo;
import com.xmutca.incubator.sso.model.OauthTokenSecret;
import com.xmutca.incubator.sso.model.SysUserInfo;
import com.xmutca.incubator.sso.service.OauthClientInfoService;
import com.xmutca.incubator.sso.service.OauthTokenSecretService;
import com.xmutca.incubator.sso.service.SysUserInfoService;
import com.xmutca.incubator.sso.vo.AuthorizeRequestVo;
import com.xmutca.incubator.sso.vo.TokenRequestVo;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务
 *
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-14
 */
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    public static final String PARAM_JTI = "jti";

    @NonNull
    private SmartValidator validator;

    @NonNull
    private StringRedisTemplate redisTemplate;

    @NonNull
    private OauthClientInfoService oauthClientInfoService;

    @NonNull
    private SysUserInfoService sysUserInfoService;

    @NonNull
    private SystemProperties systemProperties;

    @NonNull
    private OauthTokenSecretService oauthTokenSecretService;

    /**
     * 令牌服务
     *
     * @param requestVo
     * @return
     */
    @PostMapping(value = "/token")
    public Object token(@Validated @RequestBody TokenRequestVo requestVo) {
        // 检查client/secret是否存在且正确
        OauthClientInfo oauthClientInfo = getClientInfo(requestVo);
        if (null == oauthClientInfo) {
            return OauthErrorResp.INVALID_CLIENT.getResp();
        }

        if (!requestVo.getClientSecret().equals(oauthClientInfo.getSecret())) {
            return OauthErrorResp.INVALID_CLIENT.getResp();
        }

        if (!oauthClientInfo.getGrantTypes().contains(requestVo.getGrantType())) {
            return OauthErrorResp.UNSUPPORTED_GRANT_TYPE.getResp();
        }

        // 密码模式
        if (TokenRequestVo.GRANT_TYPE_PASSWORD.equals(requestVo.getGrantType())) {
            String error = validateParam(new Class<?>[]{TokenRequestVo.GrantTypePassword.class}, requestVo);
            if (StringUtils.isNotBlank(error)) {
                return OauthErrorResp.INVALID_REQUEST.getResp(error);
            }
            return tokenForPassword(requestVo);
        }

        // 刷新模式
        if (TokenRequestVo.GRANT_TYPE_REFRESH_TOKEN.equals(requestVo.getGrantType())) {
            String error = validateParam(new Class<?>[]{TokenRequestVo.GrantTypeRefreshToken.class}, requestVo);
            if (StringUtils.isNotBlank(error)) {
                return OauthErrorResp.INVALID_REQUEST.getResp(error);
            }
            return tokenForRefresh(requestVo);
        }

        // 暂不支持
        return OauthErrorResp.INVALID_REQUEST.getResp();
    }

    /**
     * 密码模式
     *
     * @param requestVo
     * @return
     */
    public Object tokenForPassword(TokenRequestVo requestVo) {
        // 检查账号
        SysUserInfo sysUserInfo = sysUserInfoService.getByUsername(requestVo.getUsername());
        if (null == sysUserInfo) {
            return OauthErrorResp.INVALID_USERNAME.getResp();
        }

        // 验证密码
        if (!DigestUtil.md5Hex(requestVo.getPassword() + sysUserInfo.getSalt()).equals(sysUserInfo.getPassword())) {
            return OauthErrorResp.INVALID_USERNAME.getResp();
        }

        // 创建新令牌
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessExpireTime = Date.from(localDateTime.plusSeconds(systemProperties.getSso().getAccessTokenExpireTimeout()).atZone(ZoneId.systemDefault()).toInstant());
        Date refreshExpireTime = Date.from(localDateTime.plusSeconds(systemProperties.getSso().getRefreshTokenExpireTimeout()).atZone(ZoneId.systemDefault()).toInstant());
        OauthTokenSecret tokenSecret = OauthTokenSecret.newInitInstance(sysUserInfo.getId(), accessExpireTime, refreshExpireTime);
        oauthTokenSecretService.save(tokenSecret);

        // 返回令牌
        String accessToken = getToken(tokenSecret.getAccessTokenId(), sysUserInfo.getId().toString(), tokenSecret.getAccessTokenSecret(), localDateTime, tokenSecret.getAccessExpireTime());
        String refreshToken = getToken(tokenSecret.getRefreshTokenId(), sysUserInfo.getId().toString(), tokenSecret.getRefreshTokenSecret(), localDateTime, tokenSecret.getRefreshExpireTime());
        return new Result<>(new TokenResponseDto("bearer", accessToken, systemProperties.getSso().getAccessTokenExpireTimeout(), refreshToken, systemProperties.getSso().getRefreshTokenExpireTimeout()));
    }

    /**
     * 获取用户令牌
     * @param jti
     * @param subject
     * @param secret
     * @param localDateTime
     * @param expireTime
     * @return
     */
    private String getToken(String jti, String subject, String secret, LocalDateTime localDateTime, Date expireTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setId(jti)
                .setIssuedAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取subject
     * @param token
     * @return
     */
    public static String getJtiFromJwtToken(String token) {
        try {
            String encodePlayLoad = token.substring(token.indexOf('.') + 1, token.lastIndexOf('.'));
            String decodePlayLoad = new String(Base64Utils.decodeFromString(encodePlayLoad));
            return JSON.parseObject(decodePlayLoad).getString(PARAM_JTI);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 刷新模式
     *
     * @param requestVo
     * @return
     */
    public Object tokenForRefresh(TokenRequestVo requestVo) {
        // 解析jti
        String jti = getJtiFromJwtToken(requestVo.getRefreshToken());
        if (null == jti) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        }

        // 判断密钥
        OauthTokenSecret tokenSecret = oauthTokenSecretService.getByRefreshTokenId(jti);
        if (null == tokenSecret) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        }

        // 判断过期
        if (tokenSecret.getRefreshExpireTime().before(new Date())) {
            return OauthErrorResp.EXPIRED_TOKEN.getResp();
        }

        // 解析信息
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSecret.getRefreshTokenSecret()).parseClaimsJws(requestVo.getRefreshToken());
            String subject = claimsJws.getBody().getSubject();
            return refreshToken(tokenSecret, jti, subject);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        } catch (ExpiredJwtException expiredEx) {
            return OauthErrorResp.EXPIRED_TOKEN.getResp();
        }
    }

    /**
     * 刷新令牌
     *
     *
     * @param tokenSecret
     * @param refreshTokenId
     * @param subject
     * @return
     */
    private Object refreshToken(OauthTokenSecret tokenSecret, String refreshTokenId, String subject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessExpireTime = Date.from(localDateTime.plusSeconds(systemProperties.getSso().getAccessTokenExpireTimeout()).atZone(ZoneId.systemDefault()).toInstant());
        OauthTokenSecret.newUpdateInstance(tokenSecret, accessExpireTime, refreshTokenId);

        // 更新并返回
        oauthTokenSecretService.updateAccessTokenIdAndSecret(tokenSecret);
        String accessToken = getToken(tokenSecret.getAccessTokenId(), subject, tokenSecret.getAccessTokenSecret(), localDateTime, tokenSecret.getAccessExpireTime());
        return new Result<>(new TokenResponseDto("bearer", accessToken, systemProperties.getSso().getAccessTokenExpireTimeout(), (tokenSecret.getRefreshExpireTime().getTime() - System.currentTimeMillis())/1000));
    }

    /**
     * 可改造为从缓存读取，如果不存在，则从数据库中读取并设置进缓存
     *
     * @param clientId
     * @return
     */
    private String getSecret(String clientId) {
        return redisTemplate.boundValueOps(getClientKey(clientId)).get();
    }

    /**
     * 可改造为从缓存读取，如果不存在，则从数据库中读取并设置进缓存
     *
     * @param requestVo
     * @return
     */
    private OauthClientInfo getClientInfo(TokenRequestVo requestVo) {
        if (StringUtils.isBlank(getSecret(requestVo.getClientId()))) {
            String lockVal = UUID.randomUUID().toString();
            try {
                boolean locked = tryGetDistributedLock("lock", lockVal, 10, TimeUnit.SECONDS);
                if (locked && StringUtils.isBlank(getSecret(requestVo.getClientId()))) {
                    // select from db
                    List<OauthClientInfo> oauthClientInfoList = oauthClientInfoService.findAll();
                    oauthClientInfoList.parallelStream().forEach(oauthClientInfo -> redisTemplate.opsForValue().set(getClientKey(oauthClientInfo.getClientId()), JSON.toJSONString(oauthClientInfo)));
                }
            } finally {
                releaseDistributedLock("lock", lockVal);
            }
        }

        String data = getSecret(requestVo.getClientId());
        if (StringUtils.isBlank(data)) {
            return null;
        }

        return JSON.parseObject(data, OauthClientInfo.class);
    }

    /**
     * 获取客户端key
     *
     * @param clientId
     * @return
     */
    private String getClientKey(String clientId) {
        return String.format("oauth_client:%s", clientId);
    }

    /**
     * 参数校验
     *
     * @param validateGroup
     * @param requestVo
     */
    public String validateParam(Class<?>[] validateGroup, TokenRequestVo requestVo) {
        BindingResult bindingResult = new BindException(requestVo.getClass(), requestVo.getClass().getName());
        validator.validate(requestVo, bindingResult, validateGroup);
        if (bindingResult.hasErrors()) {
            return fieldError2String(bindingResult.getFieldError());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 参数校验
     *
     * @param requestVo
     * @return
     */
    public String validateParam(AuthorizeRequestVo requestVo) {
        BindingResult bindingResult = new BindException(requestVo.getClass(), requestVo.getClass().getName());
        validator.validate(requestVo, bindingResult);
        if (bindingResult.hasErrors()) {
            return fieldError2String(bindingResult.getFieldError());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 字段错误转字符串
     *
     * @param fieldError Field Error
     * @return
     */
    public static String fieldError2String(FieldError fieldError) {
        return String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage());
    }

    /**
     * lock
     *
     * @param key
     * @param val
     * @return
     */
    public boolean tryGetDistributedLock(String key, String val, long timeout, TimeUnit unit) {
        try {
            boolean result = redisTemplate.opsForValue().setIfAbsent(key, val, timeout, unit);
            if (!result) {
                wait(10000);
                return tryGetDistributedLock(key, val, timeout, unit);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * release
     *
     * @param key
     * @param val
     * @return
     */
    public boolean releaseDistributedLock(String key, String val) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = redisTemplate.execute((RedisConnection connection) -> connection.eval(
                script.getBytes(), ReturnType.INTEGER, 1, key.getBytes(), val.getBytes()
        ));
        return "1L".equals(result);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Receipt logout() {
        // 获取令牌
        return null;
    }

    /**
     * @author pwh
     */
    public enum OauthErrorResp {
        INVALID_REQUEST(HttpStatus.BAD_REQUEST, "invalid_request", "非法请求"),
        INVALID_GRANT(HttpStatus.BAD_REQUEST, "invalid_grant", "非法授权"),
        UNSUPPORTED_GRANT_TYPE(HttpStatus.BAD_REQUEST, "unsupported_grant_type", "不支持的授权类型"),
        EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "expired_token", "令牌过期"),
        INVALID_TOKEN(HttpStatus.BAD_REQUEST, "invalid_token", "非法令牌"),
        INVALID_USERNAME(HttpStatus.BAD_REQUEST, "invalid_user", "用户名或者密码错误"),
        INVALID_CLIENT(HttpStatus.UNAUTHORIZED, "invalid_client", "非法客户端或者令牌");

        HttpStatus status;

        private String code;

        private String msg;

        OauthErrorResp(HttpStatus status, String code, String msg) {
            this.status = status;
            this.code = code;
            this.msg = msg;
        }

        public ResponseEntity getResp() {
            return new ResponseEntity(new Result(this.status.value(), this.msg, new TokenResponseDto(this.code, this.msg)), HttpStatus.valueOf(this.status.value()));
        }

        public ResponseEntity getResp(String msg) {
            return new ResponseEntity(new Result<>(this.status.value(), msg, new TokenResponseDto(this.code, msg)), HttpStatus.valueOf(this.status.value()));
        }
    }
}
