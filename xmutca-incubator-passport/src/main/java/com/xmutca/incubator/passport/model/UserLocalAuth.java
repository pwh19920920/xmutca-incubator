package com.xmutca.incubator.passport.model;

import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 本地登陆
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-23
 */
@Getter
@Setter
public class UserLocalAuth extends BaseEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 密码密钥
     */
    private String salt;
}
