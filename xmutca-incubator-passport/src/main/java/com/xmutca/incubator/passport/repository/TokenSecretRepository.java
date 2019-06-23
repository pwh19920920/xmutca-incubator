package com.xmutca.incubator.passport.repository;

import com.xmutca.incubator.passport.model.TokenSecret;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-16
 */
@Mapper
public interface TokenSecretRepository {

    /**
     * 获取令牌
     * @param accessTokenId
     * @return
     */
    TokenSecret getByAccessTokenId(String accessTokenId);

    /**
     * 获取令牌
     * @param refreshTokenId
     * @return
     */
    TokenSecret getByRefreshTokenId(String refreshTokenId);

    /**
     * 更新访问令牌
     * @param tokenSecret
     * @return
     */
    int updateAccessTokenIdAndSecret(TokenSecret tokenSecret);

    /**
     * 保存新令牌
     * @param tokenSecret
     * @return
     */
    int save(TokenSecret tokenSecret);

    /**
     * 更新状态
     * @param accessTokenId
     * @return
     */
    int updateStatus(String accessTokenId);
}
