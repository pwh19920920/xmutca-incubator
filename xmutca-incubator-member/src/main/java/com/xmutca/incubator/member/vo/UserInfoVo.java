package com.xmutca.incubator.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-16
 */
@Getter
@Setter
public class UserInfoVo {

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能不能为空")
    private String password;
}
