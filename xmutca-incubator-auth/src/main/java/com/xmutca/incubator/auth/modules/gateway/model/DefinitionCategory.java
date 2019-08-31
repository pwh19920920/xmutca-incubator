package com.xmutca.incubator.auth.modules.gateway.model;

import com.xmutca.incubator.core.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@Getter
@Setter
public class DefinitionCategory extends BaseEntity {

    /**
     * 类型
     */
    public enum DefinitionType {

        /**
         * 谓语
         */
        DEFINITION_TYPE_PREDICATE,

        /**
         * 过滤器
         */
        DEFINITION_TYPE_FILTER
    }

    /**
     * 参数类型
     */
    public enum ParamType {

        /**
         * 简单参数
         */
        PARAM_TYPE_SIMPLE,

        /**
         * 特殊参数
         */
        PARAM_TYPE_SPECIAL
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 定义类型
     */
    private DefinitionType definitionType;

    /**
     * 参数类型
     */
    private ParamType paramType;

    /**
     * 特殊参数列表
     */
    private List<String> specialParams = new ArrayList<>();
}
