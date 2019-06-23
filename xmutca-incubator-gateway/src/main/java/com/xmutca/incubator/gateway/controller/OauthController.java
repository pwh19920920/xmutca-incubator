package com.xmutca.incubator.gateway.controller;

import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.gateway.config.GatewayProperties;
import com.xmutca.incubator.gateway.dto.ClientInfoDto;
import com.xmutca.incubator.gateway.dto.TokenResponseDto;
import com.xmutca.incubator.gateway.dto.TokenSecretDto;
import com.xmutca.incubator.gateway.feign.PassportFeign;
import com.xmutca.incubator.gateway.helper.JwtHelper;
import com.xmutca.incubator.gateway.helper.OauthErrorHelper;
import com.xmutca.incubator.gateway.vo.TokenRequestVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.xmutca.incubator.gateway.helper.JwtHelper.TOKEN_TYPE;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-22
 */
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    @NonNull
    private PassportFeign passportFeign;

    @NonNull
    private SmartValidator validator;

    @NonNull
    private GatewayProperties gatewayProperties;

    /**
     * 令牌置换
     * 目前近支持刷新令牌、登陆获取令牌
     * @param requestVo
     * @return
     */
    @PostMapping("/token")
    public Mono token(@Validated @RequestBody TokenRequestVo requestVo, ServerWebExchange exchange) {
        Result<ClientInfoDto> clientInfoResult = passportFeign.getByClientId(requestVo.getClientId());
        ClientInfoDto oauthClientInfo = clientInfoResult.getData();
        // 判断是否存在此商户
        if (null == oauthClientInfo) {
            return OauthErrorHelper.INVALID_CLIENT.getMonoResp();
        }

        // 校验密钥是否正确
        if (!requestVo.getClientSecret().equals(oauthClientInfo.getSecret())) {
            return OauthErrorHelper.INVALID_CLIENT.getMonoResp();
        }

        // 判断是否允许此授权
        if (!oauthClientInfo.getGrantTypes().contains(requestVo.getGrantType())) {
            return OauthErrorHelper.UNSUPPORTED_GRANT_TYPE.getMonoResp();
        }

        // 密码登陆模式
        if (TokenRequestVo.GRANT_TYPE_LOGIN_PASSWORD.equals(requestVo.getGrantType())) {
            String error = validateParam(new Class<?>[]{TokenRequestVo.GrantTypeLoginPassword.class}, requestVo);
            if (StringUtils.isNotBlank(error)) {
                return OauthErrorHelper.INVALID_REQUEST.getMonoResp(error);
            }
            // 密码登陆
            return tokenForLoginPassword(requestVo);
        }

        // 刷新令牌模式
        if (TokenRequestVo.GRANT_TYPE_REFRESH_TOKEN.equals(requestVo.getGrantType())) {
            String error = validateParam(new Class<?>[]{TokenRequestVo.GrantTypeRefreshToken.class}, requestVo);
            if (StringUtils.isNotBlank(error)) {
                return OauthErrorHelper.INVALID_REQUEST.getMonoResp(error);
            }
            // 刷新令牌
            return tokenForRefresh(requestVo);
        }

        // 暂不支持
        return OauthErrorHelper.INVALID_REQUEST.getMonoResp();
    }

    /**
     * 密码登陆
     * @param requestVo
     * @return
     */
    private Mono tokenForLoginPassword(TokenRequestVo requestVo) {
        Result<Long> userResult = passportFeign.checkAndGetForUsername(requestVo.getUsername(), requestVo.getPassword());
        Long userId = userResult.getData();

        // 创建新令牌
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessExpireTime = Date.from(localDateTime.plusSeconds(gatewayProperties.getJwt().getAccessTokenExpireTimeout()).atZone(ZoneId.systemDefault()).toInstant());
        Date refreshExpireTime = Date.from(localDateTime.plusSeconds(gatewayProperties.getJwt().getRefreshTokenExpireTimeout()).atZone(ZoneId.systemDefault()).toInstant());
        TokenSecretDto tokenSecret = TokenSecretDto.newInitInstance(userId, accessExpireTime, refreshExpireTime);
        passportFeign.publish(tokenSecret);

        // 返回令牌
        String accessToken = JwtHelper.getToken(requestVo.getClientId(), userId.toString(), tokenSecret.getAccessTokenId(), tokenSecret.getAccessTokenSecret(), localDateTime, tokenSecret.getAccessExpireTime());
        String refreshToken = JwtHelper.getToken(requestVo.getClientId(), userId.toString(), tokenSecret.getRefreshTokenId(), tokenSecret.getRefreshTokenSecret(), localDateTime, tokenSecret.getRefreshExpireTime());

        // 颁发令牌
        TokenResponseDto tokenResponseDto = new TokenResponseDto(TOKEN_TYPE, accessToken, gatewayProperties.getJwt().getAccessTokenExpireTimeout(), refreshToken, gatewayProperties.getJwt().getRefreshTokenExpireTimeout());
        return Mono.just(new Result<>(tokenResponseDto));
    }

    /**
     * 刷新令牌模式
     * @param requestVo
     * @return
     */
    private Mono tokenForRefresh(TokenRequestVo requestVo) {
        // 解析jti
        JwtHelper.JwtData data = JwtHelper.getDataFromJwtToken(requestVo.getRefreshToken());
        if (StringUtils.isBlank(data.getTokenId()) || StringUtils.isBlank(data.getClientId())) {
            return OauthErrorHelper.INVALID_TOKEN.getMonoResp();
        }

        // 获取令牌
        Result<TokenSecretDto> tokenSecretResult = passportFeign.getByRefreshTokenId(data.getTokenId());
        TokenSecretDto tokenSecretDto = tokenSecretResult.getData();
        if (null == tokenSecretDto || !tokenSecretDto.isEnable()) {
            return OauthErrorHelper.INVALID_TOKEN.getMonoResp();
        }

        // 判断过期
        if (tokenSecretDto.getRefreshExpireTime().before(new Date())) {
            return OauthErrorHelper.EXPIRED_TOKEN.getMonoResp();
        }

        // 刷新处理
        return JwtHelper.parseJwtMonoHandler(
                requestVo.getRefreshToken(),
                tokenSecretDto.getRefreshTokenSecret(),
                subject -> refreshToken(tokenSecretDto, tokenSecretDto.getRefreshTokenId(), subject)
        );
    }

    /**
     * 刷新令牌
     * @param tokenSecretDto
     * @param refreshTokenId
     * @param subject
     * @return
     */
    private Mono refreshToken(TokenSecretDto tokenSecretDto, String refreshTokenId, String subject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Date accessExpireTime = Date.from(localDateTime.plusSeconds(gatewayProperties.getJwt().getAccessTokenExpireTimeout()).atZone(ZoneId.systemDefault()).toInstant());
        String oldAccessTokenId = tokenSecretDto.getAccessTokenId();
        TokenSecretDto.newUpdateInstance(tokenSecretDto, accessExpireTime, refreshTokenId);

        // 更新并返回
        passportFeign.updateAccessTokenIdAndSecret(oldAccessTokenId, tokenSecretDto);
        String accessToken = JwtHelper.getToken(tokenSecretDto.getClientId(), subject, tokenSecretDto.getAccessTokenId(), tokenSecretDto.getAccessTokenSecret(), localDateTime, tokenSecretDto.getAccessExpireTime());
        TokenResponseDto tokenResponseDto = new TokenResponseDto(TOKEN_TYPE, accessToken, gatewayProperties.getJwt().getAccessTokenExpireTimeout(), (tokenSecretDto.getRefreshExpireTime().getTime() - System.currentTimeMillis()) / 1000);
        return Mono.just(new Result<>(tokenResponseDto));
    }

    /**
     * 参数校验
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
     * 字段错误转字符串
     *
     * @param fieldError Field Error
     * @return
     */
    public static String fieldError2String(FieldError fieldError) {
        return String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage());
    }
}
