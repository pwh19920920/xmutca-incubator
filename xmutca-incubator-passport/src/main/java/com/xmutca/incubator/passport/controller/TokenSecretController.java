package com.xmutca.incubator.passport.controller;

import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.passport.model.TokenSecret;
import com.xmutca.incubator.passport.service.TokenSecretService;
import com.xmutca.incubator.passport.vo.TokenSecretVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-22
 */
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenSecretController {

    @NonNull
    private TokenSecretService tokenSecretService;

    /**
     * 获取令牌信息
     * @param accessTokenId
     * @return
     */
    @GetMapping("/access/{accessTokenId}")
    public Result<TokenSecret> getByAccessTokenId(@PathVariable String accessTokenId) {
        TokenSecret result = tokenSecretService.getByAccessTokenId(accessTokenId);
        return new Result<>(result);
    }

    /**
     * 获取令牌信息
     * @param refreshTokenId
     * @return
     */
    @GetMapping("/refresh/{refreshTokenId}")
    public Result<TokenSecret> getByRefreshTokenId(@PathVariable String refreshTokenId) {
        TokenSecret result = tokenSecretService.getByRefreshTokenId(refreshTokenId);
        return new Result<>(result);
    }

    /**
     * 更新令牌信息
     * @param oldAccessTokenId
     * @param oauthTokenSecret
     * @return
     */
    @PutMapping("/update/{oldAccessTokenId}")
    public Result<Integer> updateAccessTokenIdAndSecret(@PathVariable String oldAccessTokenId, @Validated(TokenSecretVo.RefreshToken.class) @RequestBody TokenSecretVo oauthTokenSecret) {
        int result = tokenSecretService.updateAccessTokenIdAndSecret(oldAccessTokenId, oauthTokenSecret.get());
        return new Result<>(result);
    }

    /**
     * 发布令牌
     * @param oauthTokenSecret
     * @return
     */
    @PostMapping("/publish")
    public Result<Integer> publish(@Validated(TokenSecretVo.PublishToken.class) @RequestBody TokenSecretVo oauthTokenSecret) {
        int result = tokenSecretService.save(oauthTokenSecret.get());
        return new Result<>(result);
    }

    /**
     * 注销令牌
     * @param accessTokenId
     * @param refreshTokenId
     * @return
     */
    @DeleteMapping("/logout")
    public Result<Integer> logout(String accessTokenId, String refreshTokenId) {
        if (StringUtils.isAnyEmpty(accessTokenId, refreshTokenId)) {
            throw new ServiceException("请求参数有误");
        }

        int result = tokenSecretService.updateStatus(accessTokenId);
        tokenSecretService.dropRefreshToken(refreshTokenId);
        return new Result<>(result);
    }
}
