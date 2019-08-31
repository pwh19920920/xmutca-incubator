package com.xmutca.incubator.auth.modules.gateway.repository;

import com.xmutca.incubator.auth.modules.gateway.model.RouteDefinition;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Mapper
public interface RouteDefinitionRepository {

    /**
     * 获取某个时间之后数据
     * @param time
     * @return
     */
    List<RouteDefinition> listForTimeAfter(Date time);

    /**
     * 保存
     * @param routeDefinition
     * @return
     */
    int save(RouteDefinition routeDefinition);
}
