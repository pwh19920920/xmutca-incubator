package com.xmutca.incubator.sso.service;

import com.xmutca.incubator.core.sequence.facade.Generator;
import com.xmutca.incubator.sso.model.OauthTokenSecret;
import com.xmutca.incubator.sso.repository.OauthTokenSecretRepository;
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
public class OauthTokenSecretService {

    @NonNull
    private OauthTokenSecretRepository repository;

    @NonNull
    private Generator generator;

    /**
     * 获取令牌
     * @param accessTokenId
     * @return
     */
    public OauthTokenSecret getByAccessTokenId(String accessTokenId) {
        return repository.getByAccessTokenId(accessTokenId);
    }

    /**
     * 获取令牌
     * @param refreshTokenId
     * @return
     */
    public OauthTokenSecret getByRefreshTokenId(String refreshTokenId) {
        return repository.getByRefreshTokenId(refreshTokenId);
    }

    /**
     * 更新访问令牌
     * @param tokenSecret
     * @return
     */
    public int updateAccessTokenIdAndSecret(OauthTokenSecret tokenSecret) {
        return repository.updateAccessTokenIdAndSecret(tokenSecret);
    }

    /**
     * 保存新令牌
     * @param tokenSecret
     * @return
     */
    public int save(OauthTokenSecret tokenSecret) {
        tokenSecret.setId(generator.getUid());
        return repository.save(tokenSecret);
    }

    /**
     * 更新状态
     * @param accessTokenId
     * @return
     */
    public int updateStatus(String accessTokenId) {
        return repository.updateStatus(accessTokenId);
    }
}
