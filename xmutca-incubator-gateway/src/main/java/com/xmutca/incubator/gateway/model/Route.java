package com.xmutca.incubator.gateway.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 路由
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-11
 */
@Getter
@Setter
public class Route {

    /**
     * 路由id
     */
    private Long id;

    /**
     * 唯一标识符
     */
    private String identity;

    /**
     * 转发地址
     */
    private String uri;
}
