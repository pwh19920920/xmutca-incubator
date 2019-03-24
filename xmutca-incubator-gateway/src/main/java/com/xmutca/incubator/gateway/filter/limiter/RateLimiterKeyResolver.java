package com.xmutca.incubator.gateway.filter.limiter;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 限流条件策略
 * @author weihuang.peng
 */
public class RateLimiterKeyResolver implements KeyResolver {

    /**
     * 指定根据什么限流
     * 这里根据HostAddress限流
     * @param exchange
     * @return
     */
    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}