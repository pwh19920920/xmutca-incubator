package com.xmutca.incubator.sso.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-14
 */
@Getter
@Setter
public class TokenResponseDto {

    /**
     * 访问令牌
     */
    @JSONField(name = "access_token")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @JSONField(name = "refresh_token")
    private String refreshToken;

    /**
     * 令牌类型
     */
    @JSONField(name = "token_type")
    private String tokenType;

    /**
     * 过期时间
     */
    @JSONField(name = "expires_in")
    private long expiresIn;

    /**
     * 刷新过期时间
     */
    @JSONField(name = "refresh_expires_in")
    private long refreshExpiresIn;
}