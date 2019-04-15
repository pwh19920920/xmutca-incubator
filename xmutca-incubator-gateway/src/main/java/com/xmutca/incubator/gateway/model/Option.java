package com.xmutca.incubator.gateway.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmutca.incubator.gateway.model.core.BaseRouteType;
import com.xmutca.incubator.gateway.model.core.RouteArgsType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-12
 */
@Getter
@Setter
public abstract class Option<T extends BaseRouteType> {

    /**
     * 参数转换
     * @return
     */
    public Map<String, Object> getCommonArgsMap() {
        Map<String, Object> map = Maps.newConcurrentMap();
        for (int i = 0; i < getCommonArgs().size(); i++) {
            String key = NameUtils.generateName(i);
            map.put(key, getCommonArgs().get(i));
        }
        return map;
    }

    /**
     * 检查是否合法
     * @return
     */
    public boolean checkArgs() {
        if (null == getType()) {
            return false;
        }

        RouteArgsType argsType = getType().getArgsType();
        // 判断是否无参
        if (argsType == RouteArgsType.EMPTY) {
            return true;
        }

        Map<String, Object> argsMap = getSpecialArgs();
        if (getType().getArgsType() == RouteArgsType.COMMON) {
            argsMap = getCommonArgsMap();
        }

        // 参数判断
        if (CollectionUtils.isEmpty(argsMap)) {
            return false;
        }

        // 数量判断
        if (getType().getTime() > 0 && argsMap.size() - getType().getTime() < 0) {
            return false;
        }
        return true;
    }


    /**
     * 普通参数
     */
    private List<String> commonArgs = Lists.newArrayList();

    /**
     * 特殊参数
     */
    private Map<String, Object> specialArgs = Maps.newConcurrentMap();

    /**
     * 获取类型
     * @return
     */
    abstract T getType();
}
