package com.xmutca.incubator.passport.model;

import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-23
 */
@Getter
@Setter
public class UserThirdAuth extends BaseEntity {

    public enum UserThirdAuthType {
        USER_THIRD_AUTH_TYPE_QQ,
        USER_THIRD_AUTH_TYPE_WX,
    }

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 第三方id
     */
    private String openId;

    /**
     * 当前令牌
     */
    private String accessToken;

    /**
     * 登陆类型
     */
    private UserThirdAuthType authType;
}
