package com.xmutca.incubator.auth.modules.gateway.model;

import com.xmutca.incubator.core.common.enums.StatusEnum;
import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@Getter
@Setter
public class RouteDefinition extends BaseEntity {

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 路由
     */
    private String uri;

    /**
     * 谓语列表
     */
    private List<PredicateDefinition> predicates = new ArrayList<>();

    /**
     * 过滤列表
     */
    private List<FilterDefinition> filters = new ArrayList<>();

    /**
     * 状态
     */
    private StatusEnum status = StatusEnum.STATUS_NORMAL;

    @Getter
    @Setter
    public static class PredicateDefinition {
        private String name;
        private Map<String, String> args = new LinkedHashMap<>();
    }

    @Getter
    @Setter
    public static class FilterDefinition {
        private String name;
        private Map<String, String> args = new LinkedHashMap<>();
    }
}
