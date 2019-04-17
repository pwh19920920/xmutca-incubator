package com.xmutca.incubator.sso.model;

import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Getter
@Setter
public class OauthTokenSecret extends BaseEntity {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 访问令牌ID
     */
    private String accessTokenId;

    /**
     * 访问令牌密钥
     */
    private String accessTokenSecret;

    /**
     * 访问令牌过期时间
     */
    private Date accessExpireTime;

    /**
     * 刷新令牌ID
     */
    private String refreshTokenId;

    /**
     * 刷新令牌密钥
     */
    private String refreshTokenSecret;

    /**
     * 刷新令牌过期时间
     */
    private Date refreshExpireTime;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 获取新对象
     * @param userId
     * @param accessExpireTime
     * @param refreshExpireTime
     * @return
     */
    public static OauthTokenSecret newInstance(Long userId, Date accessExpireTime, Date refreshExpireTime) {
        OauthTokenSecret tokenSecret = new OauthTokenSecret();
        tokenSecret.clientId = UUID.randomUUID().toString();
        tokenSecret.accessTokenId = UUID.randomUUID().toString();
        tokenSecret.accessTokenSecret = UUID.randomUUID().toString();
        tokenSecret.refreshTokenId = UUID.randomUUID().toString();
        tokenSecret.refreshTokenSecret = UUID.randomUUID().toString();
        tokenSecret.userId = userId;
        tokenSecret.accessExpireTime = accessExpireTime;
        tokenSecret.refreshExpireTime = refreshExpireTime;
        return tokenSecret;
    }
}
