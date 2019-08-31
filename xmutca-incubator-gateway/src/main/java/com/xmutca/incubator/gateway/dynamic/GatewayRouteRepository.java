package com.xmutca.incubator.gateway.dynamic;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-29
 */
public interface GatewayRouteRepository extends RouteDefinitionRepository {

    /**
     * batchSave
     * @param routeDefinitionFlux
     * @return
     */
    Flux<Void> batchSave(Flux<RouteDefinition> routeDefinitionFlux);

    /**
     * batchRemove
     * @param routeDefinitionFlux
     * @return
     */
    Flux<Void> batchRemove(Flux<RouteDefinition> routeDefinitionFlux);
}
