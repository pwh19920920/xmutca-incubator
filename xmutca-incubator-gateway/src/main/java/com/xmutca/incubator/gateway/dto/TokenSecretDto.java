package com.xmutca.incubator.gateway.dto;

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
public class TokenSecretDto {

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
     * 状态
     */
    private boolean enable;

    /**
     * 获取新对象
     * @param userId
     * @param accessExpireTime
     * @param refreshExpireTime
     * @return
     */
    public static TokenSecretDto newInitInstance(Long userId, Date accessExpireTime, Date refreshExpireTime) {
        TokenSecretDto tokenSecretDto = new TokenSecretDto();
        tokenSecretDto.clientId = UUID.randomUUID().toString();
        tokenSecretDto.accessTokenId = UUID.randomUUID().toString();
        tokenSecretDto.accessTokenSecret = UUID.randomUUID().toString();
        tokenSecretDto.refreshTokenId = UUID.randomUUID().toString();
        tokenSecretDto.refreshTokenSecret = UUID.randomUUID().toString();
        tokenSecretDto.userId = userId;
        tokenSecretDto.accessExpireTime = accessExpireTime;
        tokenSecretDto.refreshExpireTime = refreshExpireTime;
        return tokenSecretDto;
    }

    /**
     * 赋值数据
     * @param tokenSecretDto
     * @param accessExpireTime
     * @param refreshTokenId
     * @return
     */
    public static TokenSecretDto newUpdateInstance(TokenSecretDto tokenSecretDto, Date accessExpireTime, String refreshTokenId) {
        tokenSecretDto.accessTokenId = UUID.randomUUID().toString();
        tokenSecretDto.accessTokenSecret = UUID.randomUUID().toString();
        tokenSecretDto.refreshTokenId = refreshTokenId;
        tokenSecretDto.accessExpireTime = accessExpireTime;
        return tokenSecretDto;
    }
}
