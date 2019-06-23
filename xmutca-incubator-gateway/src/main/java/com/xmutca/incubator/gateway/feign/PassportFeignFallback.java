package com.xmutca.incubator.gateway.feign;

import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.gateway.dto.ClientInfoDto;
import com.xmutca.incubator.gateway.dto.TokenSecretDto;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Component
public class PassportFeignFallback implements PassportFeign {

    @Override
    public Result<ClientInfoDto> getByClientId(String clientId) {
        throw new ServiceException("获取商户信息为空，请稍后再试！");
    }

    @Override
    public Result<TokenSecretDto> getByAccessTokenId(String accessTokenId) {
        throw new ServiceException("获取令牌信息为空，请稍后再试！");
    }

    @Override
    public Result<TokenSecretDto> getByRefreshTokenId(String refreshTokenId) {
        throw new ServiceException("获取令牌信息为空，请稍后再试！");
    }

    @Override
    public Result<Integer> updateAccessTokenIdAndSecret(String oldAccessTokenId, TokenSecretDto oauthTokenSecretDto) {
        throw new ServiceException("更新令牌失败，请稍后再试！");
    }

    @Override
    public Result<Integer> logout(String accessTokenId, String refreshTokenId) {
        throw new ServiceException("删除令牌失败，请稍后再试！");
    }

    @Override
    public Result<Integer> publish(TokenSecretDto tokenSecretDto) {
        throw new ServiceException("发布令牌失败，请稍后再试！");
    }

    @Override
    public Result<Long> checkAndGetForUsername(String username, String password) {
        throw new ServiceException("用户名登陆失败，请稍后再试！");
    }
}
