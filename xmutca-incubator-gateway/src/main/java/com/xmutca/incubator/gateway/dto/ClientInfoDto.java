package com.xmutca.incubator.gateway.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-22
 */
@Getter
@Setter
public class ClientInfoDto {

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
