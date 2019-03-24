package com.xmutca.incubator.gateway.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-21
 */
@Slf4j
@Component
public class RequestLoggerGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestLoggerGatewayFilterFactory.Config> implements Ordered {

    public static final String PARAM_KEY = "open";

    @Override
    public int getOrder() {
        return -1;
    }

    public RequestLoggerGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARAM_KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("request before");
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        if (exchange.getResponse().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                            exchange.getResponse().bufferFactory().wrap("你被拒绝啦".getBytes());
                            exchange.getResponse().setComplete();
                        }
                        System.out.println("request after");
                    })
            );
        };
    }

    /**
     * 相关配置
     */
    @Getter
    @Setter
    public static class Config {

        /**
         * 是否开启
         */
        private boolean open;
    }
}

