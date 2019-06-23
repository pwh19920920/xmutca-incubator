package com.xmutca.incubator.passport.model;

import com.google.common.collect.Lists;
import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    /**
     * 授权类型
     */
    private List<String> grantTypes = Lists.newArrayList();
}
