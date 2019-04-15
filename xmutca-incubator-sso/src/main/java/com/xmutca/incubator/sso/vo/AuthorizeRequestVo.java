package com.xmutca.incubator.sso.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 授权对象
 *
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Getter
@Setter
public class AuthorizeRequestVo {

    public interface ResponseTypeToken {
    }

    public interface ResponseTypeCode {
    }

    /**
     * 唯一标识符
     */
    @NotBlank(message = "请求标示符不能为空", groups = {ResponseTypeCode.class})
    private String state;

    /**
     * 客户端id
     */
    @NotBlank(message = "客户端id不能为空")
    @JSONField(name = "client_id")
    private String clientId;

    /**
     * 密钥
     */
    @NotBlank(message = "密钥不能为空", groups = ResponseTypeToken.class)
    @JsonProperty("client_secret")
    private String clientSecret;

    /**
     * 响应类型
     */
    @Pattern(regexp = "(token|code)", message = "响应模式仅支持token, code")
    @JSONField(name = "response_type")
    private String responseType;

    /**
     * 跳转地址
     */
    @NotBlank(message = "跳转地址不能为空")
    @JSONField(name = "redirect_uri")
    private String redirectUri;
}
