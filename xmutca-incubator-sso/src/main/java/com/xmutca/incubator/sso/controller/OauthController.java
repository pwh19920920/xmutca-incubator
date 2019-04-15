package com.xmutca.incubator.sso.controller;

import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Receipt;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.sso.model.ClientInfo;
import com.xmutca.incubator.sso.service.ClientInfoService;
import com.xmutca.incubator.sso.vo.AuthorizeRequestVo;
import com.xmutca.incubator.sso.vo.TokenRequestVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
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

    @NonNull
    private SmartValidator validator;

    @NonNull
    private StringRedisTemplate redisTemplate;

    @NonNull
    private ClientInfoService clientInfoService;

    /**
     * 令牌服务
     * @param requestVo
     * @return
     */
    @PostMapping(value = "/token")
    public Result token(@Validated @RequestBody TokenRequestVo requestVo) {
        // 检查client/secret是否存在且正确
        if (!checkSecret(requestVo)) {
            return new Receipt(HttpStatus.BAD_REQUEST.value(), "客户端ID或密钥错误");
        }

        // 密码模式
        if (TokenRequestVo.GRANT_TYPE_PASSWORD.equals(requestVo.getGrantType())) {
            validateParam(new Class<?>[]{TokenRequestVo.GrantTypePassword.class}, requestVo);
            return tokenForPassword(requestVo);
        }

        // 刷新模式
        if (TokenRequestVo.GRANT_TYPE_REFRESH_TOKEN.equals(requestVo.getGrantType())) {
            validateParam(new Class<?>[]{TokenRequestVo.GrantTypeRefreshToken.class}, requestVo);
            return tokenForRefresh(requestVo);
        }

        // 暂不支持
        return new Receipt(HttpStatus.BAD_REQUEST.value(), "其他授权模式暂不支持");
    }

    /**
     * 密码模式
     *
     * @param requestVo
     * @return
     */
    public Result tokenForPassword(TokenRequestVo requestVo) {
        // 检查账号密码是否正确


        // 生成accessToken，refreshToken
        String accessTokenId = UUID.randomUUID().toString();
        String accessTokenSecret = UUID.randomUUID().toString();
        String refreshTokenId = UUID.randomUUID().toString();
        String refreshTokenSecret = UUID.randomUUID().toString();
        String userId = "";

        redisTemplate.boundSetOps(String.format("user_secret:%s", userId)).add(refreshTokenId);
        redisTemplate.boundValueOps(String.format("user_ticket:%s", accessTokenId)).set(accessTokenSecret, 3600, TimeUnit.SECONDS);
        redisTemplate.boundValueOps(String.format("user_refresh:%s", refreshTokenId)).set(accessTokenId, 30, TimeUnit.DAYS);
        redisTemplate.boundValueOps(String.format("user_ticket:%s", refreshTokenId)).set(refreshTokenSecret, 30, TimeUnit.DAYS);
        return new Receipt();
    }

    /**
     * 刷新模式
     *
     * @param requestVo
     * @return
     */
    public Result tokenForRefresh(TokenRequestVo requestVo) {
        return new Receipt();
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
     * @param requestVo
     * @return
     */
    private boolean checkSecret(TokenRequestVo requestVo) {
        if (StringUtils.isBlank(getSecret(requestVo.getClientId()))) {
            String lockVal = UUID.randomUUID().toString();
            try {
                boolean locked = tryGetDistributedLock("lock", lockVal, 10, TimeUnit.SECONDS);
                if (locked && StringUtils.isBlank(getSecret(requestVo.getClientId()))) {
                    // select from db
                    List<ClientInfo> clientInfoList = clientInfoService.findAll();
                    clientInfoList.parallelStream().forEach(clientInfo -> {
                        redisTemplate.opsForValue().set(getClientKey(clientInfo.getClientId()), clientInfo.getSecret());
                    });
                }
            } finally {
                releaseDistributedLock("lock", lockVal);
            }
        }
        return requestVo.getClientSecret().equals(getSecret(requestVo.getClientId()));
    }

    /**
     * 获取客户端key
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
    public void validateParam(Class<?>[] validateGroup, TokenRequestVo requestVo) {
        BindingResult bindingResult = new BindException(requestVo.getClass(), requestVo.getClass().getName());
        validator.validate(requestVo, bindingResult, validateGroup);
        if (bindingResult.hasErrors()) {
            String errorMsg = fieldError2String(bindingResult.getFieldError());
            throw new ServiceException(errorMsg);
        }
    }

    /**
     * 参数校验
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
     * @param key
     * @param val
     * @return
     */
    public boolean tryGetDistributedLock(String key, String val, long timeout, TimeUnit unit) {
         try {
             return redisTemplate.opsForValue().setIfAbsent(key, val, timeout, unit);
         } catch (Exception ex) {
             return false;
         }
    }

    /**
     * release
     * @param key
     * @param val
     * @return
     */
    public boolean releaseDistributedLock(String key, String val) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript redisScript = RedisScript.of(script);
        Object result = redisTemplate.execute(redisScript, Collections.singletonList(key), Collections.singletonList(val));
        return "1L".equals(result);
    }

    @PostMapping("/logout")
    public Receipt logout() {
        // 获取令牌
        return null;
    }
}
