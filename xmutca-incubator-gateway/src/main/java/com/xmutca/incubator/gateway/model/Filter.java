package com.xmutca.incubator.gateway.model;

import com.xmutca.incubator.gateway.core.FilterType;
import lombok.Getter;
import lombok.Setter;

/**
 * 过滤器
 *
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-11
 */
@Getter
@Setter
public class Filter extends Option<FilterType> {

    /**
     * 过滤器类型
     */
    private FilterType type;

    /**
     * 路由id
     */
    private Long routeId;
}
