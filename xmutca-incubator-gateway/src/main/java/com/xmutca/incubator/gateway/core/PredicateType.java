package com.xmutca.incubator.gateway.core;

import com.alibaba.fastjson.JSON;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-10
 */
@Getter
public enum PredicateType implements BaseRouteType {

    /**
     * 单值，可出现多次
     */
    AFTER("After", 1, -1, "在时间点之后", RouteArgsType.COMMON),

    /**
     * 单值，可出现多次
     */
    BEFORE("Before", 1, -1, "在时间点之前", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    BETWEEN("Between", 2, -1, "时间范围内", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    COOKIE("Cookie", 2, -1, "Cookie参数", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    HEADER("Header", 2, -1, "请求头参数", RouteArgsType.COMMON),

    /**
     * 多值
     */
    HOST("Host", -1, 1, "域名地址", RouteArgsType.COMMON),

    /**
     * 单值
     */
    METHOD("Method", 1, 1, "请求方法", RouteArgsType.COMMON),

    /**
     * 多值
     */
    PATH("Path", -1, 1, "访问地址", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    QUERY("Query", 2, -1, "请求参数", RouteArgsType.COMMON),

    /**
     * 多值
     */
    REMOTE_ADDR("RemoteAddr", -1, 1, "请求iP地址", RouteArgsType.COMMON),

    /**
     * 两值
     */
    WEIGHT("Weight", 2, 1, "服务权重", RouteArgsType.COMMON);

    /**
     * 缩写
     */
    private String abbr;

    /**
     * 次数
     */
    private int time;

    /**
     * 行数
     */
    private int line;

    /**
     * 描述
     */
    private String desc;

    /**
     * 参数类型
     */
    private RouteArgsType argsType;

    PredicateType(String abbr, int time, int line, String desc, RouteArgsType argsType) {
        this.abbr = abbr;
        this.line = line;
        this.time = time;
        this.desc = desc;
        this.argsType = argsType;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(Arrays.asList(PredicateType.values()).parallelStream().map(PredicateType::toMap).collect(Collectors.toList())));
    }
}