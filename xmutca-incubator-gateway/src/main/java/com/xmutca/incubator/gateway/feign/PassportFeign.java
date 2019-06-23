package com.xmutca.incubator.gateway.feign;

import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.gateway.dto.ClientInfoDto;
import com.xmutca.incubator.gateway.dto.TokenSecretDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Component
@FeignClient(name = "xmutca-incubator-passport", fallback = PassportFeignFallback.class)
public interface PassportFeign {

    /**
     * 通过客户端id获取数据
     * @param clientId
     * @return
     */
    @GetMapping("/client/{clientId}")
    Result<ClientInfoDto> getByClientId(@PathVariable("clientId") String clientId);

    /**
     * 获取令牌信息
     * @param accessTokenId
     * @return
     */
    @GetMapping("/token/access/{accessTokenId}")
    Result<TokenSecretDto> getByAccessTokenId(@PathVariable("accessTokenId") String accessTokenId);

    /**
     * 获取令牌信息
     * @param refreshTokenId
     * @return
     */
    @GetMapping("/token/refresh/{refreshTokenId}")
    Result<TokenSecretDto> getByRefreshTokenId(@PathVariable("refreshTokenId") String refreshTokenId);

    /**
     * 更新令牌信息
     * @param oldAccessTokenId
     * @param oauthTokenSecretDto
     * @return
     */
    @PutMapping("/token/update/{oldAccessTokenId}")
    Result<Integer> updateAccessTokenIdAndSecret(@PathVariable("oldAccessTokenId") String oldAccessTokenId, @RequestBody TokenSecretDto oauthTokenSecretDto);

    /**
     * 注销令牌
     * @param accessTokenId
     * @param refreshTokenId
     * @return
     */
    @DeleteMapping("/token/logout")
    Result<Integer> logout(@RequestParam("accessTokenId") String accessTokenId, @RequestParam("refreshTokenId") String refreshTokenId);

    /**
     * 发布令牌
     * @param tokenSecretDto
     * @return
     */
    @PostMapping("/token/publish")
    Result<Integer> publish(@RequestBody TokenSecretDto tokenSecretDto);

    /**
     * 密码登陆
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/auth/password")
    Result<Long> checkAndGetForUsername(@RequestParam("username") String username, @RequestParam("password") String password);
}
