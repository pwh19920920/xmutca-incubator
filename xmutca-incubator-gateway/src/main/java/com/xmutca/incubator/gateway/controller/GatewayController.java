package com.xmutca.incubator.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-09
 */
@RestController
public class GatewayController {

    @Autowired
    private GatewayProperties gatewayProperties;

    @GetMapping("/gateway")
    public Mono<List<RouteDefinition>> proxy() throws Exception {
        return Mono.just(gatewayProperties.getRoutes());
    }

    /**
     * 1. save route主体
     * 2. save filter
     * 3. save predicate
     */

}
