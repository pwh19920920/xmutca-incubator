package com.xmutca.incubator.gateway.model;

import com.xmutca.incubator.gateway.model.core.PredicateType;
import lombok.Getter;
import lombok.Setter;

/**
 * 断言
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-11
 */
@Getter
@Setter
public class Predicate extends Option<PredicateType> {

    /**
     * 断言类型
     */
    private PredicateType type;

    /**
     * 路由id
     */
    private Long routeId;
}
