package com.xmutca.incubator.gateway.core;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xmutca.incubator.core.logger.message.ServiceLoggerMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-10
 */
@Slf4j
@Getter
public class Route {

    /**
     * 断言
     */
    private Map<BaseRouteType, List<RouteParam>> predicates = new ConcurrentHashMap<>();

    /**
     * 过滤器
     */
    private Map<BaseRouteType, List<RouteParam>> filters = new ConcurrentHashMap<>();

    /**
     * 放入断言
     * @param type
     * @param param
     */
    public void putPredicate(PredicateType type, RouteParam param) {
        putParam(predicates, type, param);
    }

    /**
     * 放入断言
     * @param type
     * @param param
     */
    public void putFilter(FilterType type, RouteParam param) {
        putParam(filters, type, param);
    }

    /**
     * 放入断言
     * @param type
     * @param param
     */
    public void putParam(Map<BaseRouteType, List<RouteParam>> map, BaseRouteType type, RouteParam param) {

        // 防止空
        map.putIfAbsent(type, Lists.newArrayList());

        // 判断是否无参
        if (type.getArgsType() == RouteArgsType.EMPTY) {
            return;
        }

        Map<String, Object> argsMap = param.getSpecialArgs();
        if (type.getArgsType() == RouteArgsType.COMMON) {
            argsMap = param.getCommonArgsMap();
        }

        // 参数判断
        if (CollectionUtils.isEmpty(argsMap)) {
            ServiceLoggerMessage.getInstance("current predicate param is illegal value", "{}", type).info(log);
            return;
        }

        if (type.getTime() > 0 && argsMap.size() - type.getTime() < 0) {
            ServiceLoggerMessage.getInstance("current predicate param is illegal value", "{}", type).info(log);
            return;
        }

        // 同步防止计算出错
        synchronized (predicates) {
            List<RouteParam> predicateList = map.get(type);
            if (type.getLine() > 0 && predicateList.size() - type.getLine() >= 0) {
                ServiceLoggerMessage.getInstance("current predicate type is too much line", "{}", type).info(log);
                return;
            }

            predicateList.add(param);
        }
    }

    public static void main(String[] args) {
        Route route = new Route();
        route.putPredicate(PredicateType.BETWEEN, new RouteParam(Lists.newArrayList("*.aaa.com", "*.bbb.com")));
        route.putPredicate(PredicateType.BETWEEN, new RouteParam(Lists.newArrayList("*.ddd.com", "*.eee.com")));
        route.putPredicate(PredicateType.HOST, new RouteParam(Lists.newArrayList("*.3333.com", "*.444.com")));
        System.out.println(JSON.toJSONString(route));
    }
}
