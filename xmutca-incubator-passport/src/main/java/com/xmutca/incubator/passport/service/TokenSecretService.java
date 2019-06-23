package com.xmutca.incubator.passport.service;

import com.alicp.jetcache.anno.*;
import com.xmutca.incubator.core.sequence.facade.Generator;
import com.xmutca.incubator.passport.model.TokenSecret;
import com.xmutca.incubator.passport.repository.TokenSecretRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Service
@RequiredArgsConstructor
public class TokenSecretService {

    @NonNull
    private TokenSecretRepository repository;

    @NonNull
    private Generator generator;

    /**
     * 获取令牌
     * @param accessTokenId
     * @return
     */
    @CachePenetrationProtect
    @Cached(expire = 600, localExpire = 120, cacheType = CacheType.BOTH, name="sso:access_token_id:", key = "#accessTokenId")
    @CacheRefresh(refresh = 120, stopRefreshAfterLastAccess = 600)
    public TokenSecret getByAccessTokenId(String accessTokenId) {
        return repository.getByAccessTokenId(accessTokenId);
    }

    /**
     * 获取令牌
     * @param refreshTokenId
     * @return
     */
    @CachePenetrationProtect
    @Cached(expire = 600, localExpire = 120, cacheType = CacheType.BOTH, name="sso:refresh_token_id:", key = "#refreshTokenId")
    @CacheRefresh(refresh = 120, stopRefreshAfterLastAccess = 600)
    public TokenSecret getByRefreshTokenId(String refreshTokenId) {
        return repository.getByRefreshTokenId(refreshTokenId);
    }

    /**
     * 更新访问令牌
     *
     * @param oldAccessTokenId
     * @param tokenSecret
     * @return
     */
    @CacheInvalidate(name="sso:access_token_id:", key = "#oldAccessTokenId")
    public int updateAccessTokenIdAndSecret(String oldAccessTokenId, TokenSecret tokenSecret) {
        return repository.updateAccessTokenIdAndSecret(tokenSecret);
    }

    /**
     * 保存新令牌
     * @param tokenSecret
     * @return
     */
    public int save(TokenSecret tokenSecret) {
        tokenSecret.setId(generator.getUid());
        return repository.save(tokenSecret);
    }

    /**
     * 更新状态
     * @param accessTokenId
     * @return
     */
    @CacheInvalidate(name="sso:access_token_id:", key = "#accessTokenId")
    public int updateStatus(String accessTokenId) {
        return repository.updateStatus(accessTokenId);
    }
}
