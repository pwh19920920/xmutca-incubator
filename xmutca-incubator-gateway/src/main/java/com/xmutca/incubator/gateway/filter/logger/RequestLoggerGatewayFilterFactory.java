package com.xmutca.incubator.gateway.filter.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-21
 */
@Slf4j
@Component
public class RequestLoggerGatewayFilterFactory extends AbstractGatewayFilterFactory implements Ordered {

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            log.info("system request for path: {}", exchange.getRequest().getPath());
            return chain.filter(exchange);
        };
    }
}

