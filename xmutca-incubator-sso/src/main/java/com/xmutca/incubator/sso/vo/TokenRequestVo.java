package com.xmutca.incubator.sso.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-14
 */
@Getter
@Setter
public class TokenRequestVo {

    public static final String GRANT_TYPE_PASSWORD = "password";
    public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    public interface GrantTypePassword {

    }

    public interface GrantTypeRefreshToken {

    }

    /**
     * 授权类型
     * password:            此种类型需要client_id，client_secret,username,password必传
     * authorization_code:  此种类型需要client_id，client_secret,code必传
     * refresh_token：       此种类型需要client_id，client_secret,refresh_token必传
     * client_credentials：  此种类型需要client_id，client_secret必传
     */
    @NotBlank(message = "授权类型不能为空")
    @Pattern(regexp = "(password|authorization_code|refresh_token|client_credentials)", message = "授权类型仅支持password, authorization_code, refresh_token, client_credentials")
    @JSONField(name = "grant_type")
    private String grantType;

    /**
     * 客户端id
     */
    @NotBlank(message = "客户端id不能为空")
    @JSONField(name = "client_id")
    private String clientId;

    /**
     * 密钥
     */
    @NotBlank(message = "密钥不能为空")
    @JSONField(name = "client_secret")
    private String clientSecret;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {GrantTypePassword.class})
    private String username;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空", groups = {GrantTypePassword.class})
    private String password;

    /**
     * 授权回调过来的code
     */
    private String code;

    /**
     * 用来刷新access_token
     * 刷新后旧的access_token不可用
     */
    @NotBlank(message = "刷新令牌不能为空", groups = {GrantTypeRefreshToken.class})
    @JSONField(name = "refresh_token")
    private String refreshToken;

    /**
     * 获取key
     * @return
     */
    public String getClientKey() {
        return String.format("%s:%s", getClientId(), getClientSecret());
    }
}