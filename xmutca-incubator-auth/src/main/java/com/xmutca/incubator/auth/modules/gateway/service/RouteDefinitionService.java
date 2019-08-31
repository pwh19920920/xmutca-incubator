package com.xmutca.incubator.auth.modules.gateway.service;

import com.xmutca.incubator.auth.modules.gateway.model.RouteDefinition;
import com.xmutca.incubator.auth.modules.gateway.repository.RouteDefinitionRepository;
import com.xmutca.incubator.core.sequence.facade.Generator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@Service
@RequiredArgsConstructor
public class RouteDefinitionService {

    @NonNull
    private RouteDefinitionRepository repository;

    @NonNull
    private Generator generator;

    public int save(RouteDefinition routeDefinition) {
        routeDefinition.setId(generator.getUid());
        return repository.save(routeDefinition);
    }

    public List<RouteDefinition> listForTimeAfter(long time) {
        return repository.listForTimeAfter(new Date(time));
    }
}
