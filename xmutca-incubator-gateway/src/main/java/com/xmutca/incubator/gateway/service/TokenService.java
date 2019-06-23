package com.xmutca.incubator.gateway.service;

import com.xmutca.incubator.core.common.constant.RequestConstant;
import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.gateway.dto.TokenSecretDto;
import com.xmutca.incubator.gateway.feign.PassportFeign;
import com.xmutca.incubator.gateway.helper.JwtHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-22
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    @NonNull
    private PassportFeign passportFeign;

    /**
     * 获取用户ID
     * @param request
     * @return
     */
    public String checkAndGetSubject(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst(RequestConstant.REQUEST_HEADER_TOKEN);
        String token = JwtHelper.getToken(authorization);
        TokenSecretDto tokenSecretDto = checkAndGetTokenSecretDto(token, request);

        // 信息解密
        return JwtHelper.parseJwtHandler(token, tokenSecretDto.getAccessTokenSecret());
    }

    /**
     * 获取用户ID
     * @param request
     * @return
     */
    public TokenSecretDto checkAndGetTokenSecret(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst(RequestConstant.REQUEST_HEADER_TOKEN);
        String token = JwtHelper.getToken(authorization);
        TokenSecretDto tokenSecretDto = checkAndGetTokenSecretDto(token, request);
        JwtHelper.parseJwtHandler(token, tokenSecretDto.getAccessTokenSecret());
        return tokenSecretDto;
    }

    /**
     * 获取令牌信息
     * @param token
     * @param request
     * @return
     */
    public TokenSecretDto checkAndGetTokenSecretDto(String token , ServerHttpRequest request) {
        // 简单校验令牌
        JwtHelper.JwtData data = JwtHelper.getDataFromAuthorization(token);
        if (StringUtils.isBlank(data.getTokenId()) || StringUtils.isBlank(data.getClientId())) {
            throw new ServiceException("令牌格式有误");
        }

        // 令牌解密
        Result<TokenSecretDto> tokenSecretResult = passportFeign.getByAccessTokenId(data.getTokenId());
        TokenSecretDto tokenSecretDto = tokenSecretResult.getData();
        if (null == tokenSecretDto || !tokenSecretDto.isEnable()) {
            throw new ServiceException("密钥信息不存在");
        }

        // 判断过期
        if (tokenSecretDto.getRefreshExpireTime().before(new Date())) {
            throw new ServiceException("密钥信息已过期");
        }
        return tokenSecretDto;
    }
}
