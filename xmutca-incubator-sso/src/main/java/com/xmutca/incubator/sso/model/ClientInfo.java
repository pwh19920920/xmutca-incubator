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
public class ClientInfo extends BaseEntity {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 密钥
     */
    private String secret;
}
