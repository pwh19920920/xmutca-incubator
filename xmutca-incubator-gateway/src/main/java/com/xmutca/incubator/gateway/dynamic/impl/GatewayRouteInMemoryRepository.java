package com.xmutca.incubator.gateway.dynamic.impl;

import com.xmutca.incubator.gateway.dynamic.GatewayRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-29
 */
@Component
@RequiredArgsConstructor
public class GatewayRouteInMemoryRepository implements GatewayRouteRepository {

    private static final Map<String, RouteDefinition> ROUTE_DEFINITION_MAP = synchronizedMap(new LinkedHashMap<>());

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            ROUTE_DEFINITION_MAP.put(r.getId(), r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (ROUTE_DEFINITION_MAP.containsKey(id)) {
                ROUTE_DEFINITION_MAP.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(ROUTE_DEFINITION_MAP.values());
    }

    @Override
    public Flux<Void> batchSave(Flux<RouteDefinition> routeDefinitionFlux) {
        return routeDefinitionFlux.flatMap(r -> {
            ROUTE_DEFINITION_MAP.put(r.getId(), r);
            return Mono.empty();
        });
    }

    @Override
    public Flux<Void> batchRemove(Flux<RouteDefinition> routeDefinitionFlux) {
        return routeDefinitionFlux.flatMap(r -> {
            ROUTE_DEFINITION_MAP.remove(r.getId());
            return Mono.empty();
        });
    }
}
