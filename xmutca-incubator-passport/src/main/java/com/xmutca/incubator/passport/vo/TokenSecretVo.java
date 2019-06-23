package com.xmutca.incubator.passport.vo;

import cn.hutool.core.bean.BeanUtil;
import com.xmutca.incubator.passport.model.TokenSecret;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-23
 */
@Getter
@Setter
public class TokenSecretVo {

    /**
     * 刷新令牌
     */
    public interface RefreshToken {

    }

    /**
     * 发布令牌
     */
    public interface PublishToken {

    }

    /**
     * 客户端id
     */
    @NotBlank(groups = PublishToken.class, message = "客户端id不能为空")
    private String clientId;

    /**
     * 访问令牌ID
     */
    @NotBlank(groups = {RefreshToken.class, PublishToken.class}, message = "访问令牌ID不能为空")
    private String accessTokenId;

    /**
     * 访问令牌密钥
     */
    @NotBlank(groups = {RefreshToken.class, PublishToken.class}, message = "访问令牌密钥不能为空")
    private String accessTokenSecret;

    /**
     * 访问令牌过期时间
     */
    @NotNull(groups = {RefreshToken.class, PublishToken.class}, message = "访问令牌过期时间不能为空")
    private Date accessExpireTime;

    /**
     * 刷新令牌ID
     */
    @NotBlank(groups = {RefreshToken.class, PublishToken.class}, message = "刷新令牌ID不能为空")
    private String refreshTokenId;

    /**
     * 刷新令牌密钥
     */
    @NotBlank(groups = PublishToken.class, message = "刷新令牌密钥不能为空")
    private String refreshTokenSecret;

    /**
     * 刷新令牌过期时间
     */
    @NotNull(groups = PublishToken.class, message = "刷新令牌过期时间不能为空")
    private Date refreshExpireTime;

    /**
     * 用户ID
     */
    @NotNull(groups = PublishToken.class, message = "用户ID不能为空")
    private Long userId;

    /**
     * 获取原对象
     * @return
     */
    public TokenSecret get() {
        TokenSecret tokenSecret = new TokenSecret();
        BeanUtil.copyProperties(this, tokenSecret);
        return tokenSecret;
    }
}
