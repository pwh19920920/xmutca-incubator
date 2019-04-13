package com.xmutca.incubator.gateway.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.support.NameUtils;

import java.util.List;
import java.util.Map;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-10
 */
@Getter
@Setter
public class RouteParam {

    /**
     * 参数
     */
    private List<String> commonArgs = Lists.newArrayList();

    /**
     * 特殊参数
     */
    private Map<String, Object> specialArgs = Maps.newConcurrentMap();

    /**
     * 参数转换
     * @return
     */
    public Map<String, Object> getCommonArgsMap() {
        Map<String, Object> map = Maps.newConcurrentMap();
        for (int i = 0; i < commonArgs.size(); i++) {
           String key = NameUtils.generateName(i);
            map.put(key, commonArgs.get(i));
        }
        return map;
    }

    public RouteParam() {

    }

    public RouteParam(List<String> commonArgs) {
        this.commonArgs = commonArgs;
    }

    public RouteParam(Map<String, Object> specialArgs) {
        this.specialArgs = specialArgs;
    }
}
