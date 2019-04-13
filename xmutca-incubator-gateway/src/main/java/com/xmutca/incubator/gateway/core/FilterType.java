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
public enum FilterType implements BaseRouteType {

    /**
     * 两值，可出现多次
     */
    ADD_REQUEST_HEADER("AddRequestHeader", 2, -1, "添加请求头", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    ADD_REQUEST_PARAMETER("AddRequestParameter", 2, -1, "添加请求参数", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    ADD_RESPONSE_HEADER("AddResponseHeader", 2, -1, "添加响应头", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    HYSTRIX("Hystrix", 2, -1, "熔断参数", RouteArgsType.SPECIAL),

    /**
     * 两值，可出现多次
     */
    FALLBACK_HEADERS("FallbackHeaders", 2, -1, "熔断请求头", RouteArgsType.SPECIAL),

    /**
     * 单值
     */
    PREFIX_PATH("PrefixPath", 1, 1, "转发服务前缀", RouteArgsType.COMMON),

    /**
     * 无值
     */
    PRESERVE_HOST_HEADER("PreserveHostHeader", 1, 1, "转发原始请求头", RouteArgsType.EMPTY),

    /**
     * 两值，可出现多次
     */
    RATE_LIMITER("RateLimiter", 2, -1, "请求限流", RouteArgsType.SPECIAL),

    /**
     * 两值
     */
    REDIRECT_TO("RedirectTo", 2, 1, "重定向", RouteArgsType.COMMON),

    /**
     * 单值，可出现多次
     */
    REMOVE_REQUEST_HEADER("RemoveRequestHeader", 1, -1, "转发删除请求头", RouteArgsType.COMMON),

    /**
     * 单值
     */
    SET_PATH("SetPath", 1, 1, "转发地址设置", RouteArgsType.COMMON),

    /**
     * 单值
     */
    STRIP_PREFIX("StripPrefix", 1, 1, "忽略前缀截数", RouteArgsType.COMMON),

    /*
     * 两值
     */
    REWRITE_PATH("RewritePath", 2, 1, "转发地址重写", RouteArgsType.COMMON),

    /*
     * 两值
     */
    REQUEST_SIZE("RequestSize", 2, -1, "请求大小限制", RouteArgsType.SPECIAL),

    /*
     * 三值
     */
    REWRITE_RESPONSE_HEADER("RewriteResponseHeader", 3, -1, "替换响应请求头", RouteArgsType.COMMON),

    /**
     * 单值，可出现多次
     */
    REMOVE_RESPONSE_HEADER("RemoveResponseHeader", 1, -1, "删除响应请求头", RouteArgsType.COMMON),

    /**
     * 两值，可出现多次
     */
    SET_RESPONSE_HEADER("SetResponseHeader", 2, -1, "设置响应请求头", RouteArgsType.COMMON);

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

    FilterType(String abbr, int time, int line, String desc, RouteArgsType argsType) {
        this.abbr = abbr;
        this.line = line;
        this.time = time;
        this.desc = desc;
        this.argsType = argsType;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(Arrays.asList(FilterType.values()).parallelStream().map(FilterType::toMap).collect(Collectors.toList())));
    }
}