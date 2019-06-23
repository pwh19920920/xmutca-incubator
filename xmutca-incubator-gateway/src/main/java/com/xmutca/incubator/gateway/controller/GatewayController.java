package com.xmutca.incubator.gateway.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RestController
public class GatewayController {

    @NonNull
    private GatewayProperties gatewayProperties;

    @GetMapping("/gateway")
    public Mono<List<RouteDefinition>> proxy() throws Exception {
        return Mono.just(gatewayProperties.getRoutes());
    }
}
