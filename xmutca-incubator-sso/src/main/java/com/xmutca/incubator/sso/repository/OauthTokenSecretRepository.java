package com.xmutca.incubator.sso.repository;

import com.xmutca.incubator.sso.model.OauthTokenSecret;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-16
 */
@Mapper
public interface OauthTokenSecretRepository {

    /**
     * 获取令牌
     * @param accessTokenId
     * @return
     */
    OauthTokenSecret getByAccessTokenId(String accessTokenId);

    /**
     * 获取令牌
     * @param refreshTokenId
     * @return
     */
    OauthTokenSecret getByRefreshTokenId(String refreshTokenId);

    /**
     * 更新访问令牌
     * @param tokenSecret
     * @return
     */
    int updateAccessTokenIdAndSecret(OauthTokenSecret tokenSecret);

    /**
     * 保存新令牌
     * @param tokenSecret
     * @return
     */
    int save(OauthTokenSecret tokenSecret);

    /**
     * 更新状态
     * @param accessTokenId
     * @return
     */
    int updateStatus(String accessTokenId);
}
