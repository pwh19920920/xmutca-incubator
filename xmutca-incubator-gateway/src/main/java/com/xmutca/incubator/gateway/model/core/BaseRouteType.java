package com.xmutca.incubator.gateway.model.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-11
 */
public interface BaseRouteType {

    /**
     * 获取参数说明
     * @return
     */
    default Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", name());
        map.put("abbr", getAbbr());
        map.put("line", getLine());
        map.put("time", getTime());
        map.put("desc", getDesc());
        map.put("argsType", getArgsType());
        return map;
    }

    /**
     * 获取名字
     * @return
     */
    String name();

    /**
     * 获取缩写
     * @return
     */
    String getAbbr();

    /**
     * 获取行数
     * @return
     */
    int getLine();

    /**
     * 获取次数
     * @return
     */
    int getTime();

    /**
     * 获取描述
     * @return
     */
    String getDesc();

    /**
     * 获取参数类型
     * @return
     */
    RouteArgsType getArgsType();
}
