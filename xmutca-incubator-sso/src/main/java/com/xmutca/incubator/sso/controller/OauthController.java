package com.xmutca.incubator.sso.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmutca.incubator.core.common.constant.RequestConstant;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
    public static final String PARAM_CLIENT = "client";
    public static final String TOKEN_KEY = RequestConstant.REQUEST_HEADER_TOKEN;
    public static final String TOKEN_TYPE = "bearer";

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
        OauthClientInfo oauthClientInfo = oauthClientInfoService.getByClientId(requestVo.getClientId());
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
        String accessToken = getToken(requestVo.getClientId(), tokenSecret.getAccessTokenId(), sysUserInfo.getId().toString(), tokenSecret.getAccessTokenSecret(), localDateTime, tokenSecret.getAccessExpireTime());
        String refreshToken = getToken(requestVo.getClientId(), tokenSecret.getRefreshTokenId(), sysUserInfo.getId().toString(), tokenSecret.getRefreshTokenSecret(), localDateTime, tokenSecret.getRefreshExpireTime());
        return new Result<>(new TokenResponseDto(TOKEN_TYPE, accessToken, systemProperties.getSso().getAccessTokenExpireTimeout(), refreshToken, systemProperties.getSso().getRefreshTokenExpireTimeout()));
    }

    /**
     * 获取用户令牌
     *
     * @param clientId
     * @param jti
     * @param subject
     * @param secret
     * @param localDateTime
     * @param expireTime
     * @return
     */
    private String getToken(String clientId, String jti, String subject, String secret, LocalDateTime localDateTime, Date expireTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setId(jti)
                .claim(PARAM_CLIENT, clientId)
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
    public static JSONObject getDataFromJwtToken(String token) {
        try {
            String encodePlayLoad = token.substring(token.indexOf('.') + 1, token.lastIndexOf('.'));
            String decodePlayLoad = new String(Base64Utils.decodeFromString(encodePlayLoad));
            return JSON.parseObject(decodePlayLoad);
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
        JSONObject data = getDataFromJwtToken(requestVo.getRefreshToken());
        if (null == data || StringUtils.isBlank(data.getString(PARAM_JTI)) || StringUtils.isBlank(data.getString(PARAM_CLIENT))) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        }

        // 判断密钥
        OauthTokenSecret tokenSecret = oauthTokenSecretService.getByRefreshTokenId(data.getString(PARAM_JTI));
        if (null == tokenSecret || !tokenSecret.isEnable()) {
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
            return refreshToken(tokenSecret, data.getString(PARAM_JTI), subject);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        } catch (ExpiredJwtException expiredEx) {
            return OauthErrorResp.EXPIRED_TOKEN.getResp();
        }
    }

    /**
     * 方法
     */
    interface Func {

        /**
         * 获取执行结果
         * @return
         * @param tokenSecret
         */
        Object execute(OauthTokenSecret tokenSecret);
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
        String oldAccessTokenId = tokenSecret.getAccessTokenId();
        OauthTokenSecret.newUpdateInstance(tokenSecret, accessExpireTime, refreshTokenId);

        // 更新并返回
        oauthTokenSecretService.updateAccessTokenIdAndSecret(oldAccessTokenId, tokenSecret);
        String accessToken = getToken(tokenSecret.getClientId(), tokenSecret.getAccessTokenId(), subject, tokenSecret.getAccessTokenSecret(), localDateTime, tokenSecret.getAccessExpireTime());
        return new Result<>(new TokenResponseDto(TOKEN_TYPE, accessToken, systemProperties.getSso().getAccessTokenExpireTimeout(), (tokenSecret.getRefreshExpireTime().getTime() - System.currentTimeMillis())/1000));
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
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Object logout(HttpServletRequest request) {
        return checkAndExecute(request, (OauthTokenSecret tokenSecret) -> {
            oauthTokenSecretService.updateStatus(tokenSecret.getAccessTokenId());
            return new Result<>("退出成功");
        });
    }

    /**
     * 获取用户id
     * @param request
     * @return
     */
    @GetMapping("/checkAndGetUserId")
    public Object check(HttpServletRequest request) {
        return checkAndExecute(request, (OauthTokenSecret tokenSecret) -> new Result<Long>(tokenSecret.getUserId()));
    }

    /**
     * 检查令牌并执行
     * @param request
     * @param func
     * @return
     */
    public Object checkAndExecute(HttpServletRequest request, Func func) {
        String authorization = request.getHeader(TOKEN_KEY);
        if (StringUtils.isBlank(authorization) || authorization.length() < TOKEN_TYPE.length() + 1) {
            return OauthErrorResp.INVALID_CLIENT.getResp();
        }

        String token = authorization.substring(TOKEN_TYPE.length() + 1);
        JSONObject data = getDataFromJwtToken(token);
        if (null == data || StringUtils.isBlank(data.getString(PARAM_JTI)) || StringUtils.isBlank(data.getString(PARAM_CLIENT))) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        }

        // 判断密钥
        OauthTokenSecret tokenSecret = oauthTokenSecretService.getByAccessTokenId(data.getString(PARAM_JTI));
        if (null == tokenSecret || !tokenSecret.isEnable()) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        }

        // 判断过期
        if (tokenSecret.getAccessExpireTime().before(new Date())) {
            return OauthErrorResp.EXPIRED_TOKEN.getResp();
        }

        // 解析信息
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSecret.getAccessTokenSecret()).parseClaimsJws(token);
            claimsJws.getBody().getSubject();
            return func.execute(tokenSecret);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            return OauthErrorResp.INVALID_TOKEN.getResp();
        } catch (ExpiredJwtException expiredEx) {
            return OauthErrorResp.EXPIRED_TOKEN.getResp();
        }
    }

    /**
     * 授权错误描述
     * @author pwh
     */
    public enum OauthErrorResp {

        /**
         * 非法请求
         */
        INVALID_REQUEST(HttpStatus.BAD_REQUEST, "invalid_request", "非法请求"),

        /**
         * 非法授权
         */
        INVALID_GRANT(HttpStatus.BAD_REQUEST, "invalid_grant", "非法授权"),

        /**
         * 不支持的授权类型
         */
        UNSUPPORTED_GRANT_TYPE(HttpStatus.BAD_REQUEST, "unsupported_grant_type", "不支持的授权类型"),

        /**
         * 令牌过期
         */
        EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "expired_token", "令牌过期"),

        /**
         * 非法令牌
         */
        INVALID_TOKEN(HttpStatus.BAD_REQUEST, "invalid_token", "非法令牌"),

        /**
         * 用户名或者密码错误
         */
        INVALID_USERNAME(HttpStatus.BAD_REQUEST, "invalid_user", "用户名或者密码错误"),

        /**
         * 非法客户端或者令牌
         */
        INVALID_CLIENT(HttpStatus.UNAUTHORIZED, "invalid_client", "非法客户端或者令牌");

        /**
         * 状态
         */
        HttpStatus status;

        /**
         * 错误码
         */
        private String code;

        /**
         * 错误秒数
         */
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
