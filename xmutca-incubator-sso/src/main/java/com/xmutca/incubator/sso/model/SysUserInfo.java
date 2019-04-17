package com.xmutca.incubator.sso.model;

import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Getter
@Setter
public class SysUserInfo extends BaseEntity {

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码盐
     */
    private String salt;
}
