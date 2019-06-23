package com.xmutca.incubator.gateway.filter.limiter;

import com.xmutca.incubator.gateway.helper.ResultHelper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

/**
 * 限流网关过滤器
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
@Getter
@Setter
@ConfigurationProperties("spring.cloud.gateway.filter.request-rate-limiter")
public class RateLimiterGatewayFilterFactory extends RequestRateLimiterGatewayFilterFactory implements Ordered {

    private final RateLimiter defaultRateLimiter;

    private final KeyResolver defaultKeyResolver;

    private static final String EMPTY_KEY = "____EMPTY_KEY__";

    /** HttpStatus to return when denyEmptyKey is true, defaults to FORBIDDEN. */
    private String emptyKeyStatusCode = HttpStatus.FORBIDDEN.name();

    /**
     * Switch to deny requests if the Key Resolver returns an empty key, defaults to true.
     */
    private boolean denyEmptyKey = true;

    public RateLimiterGatewayFilterFactory(RateLimiter defaultRateLimiter, KeyResolver defaultKeyResolver) {
        super(defaultRateLimiter, defaultKeyResolver);

        this.defaultRateLimiter = defaultRateLimiter;
        this.defaultKeyResolver = defaultKeyResolver;
    }

    @Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = getOrDefaultValue(config.getKeyResolver(), defaultKeyResolver);
        RateLimiter<Object> limiter = getOrDefaultValue(config.getRateLimiter(), defaultRateLimiter);
        boolean denyEmpty = getOrDefaultValue(config.getDenyEmptyKey(), denyEmptyKey);
        HttpStatusHolder emptyKeyStatus = HttpStatusHolder.parse(getOrDefaultValue(config.getEmptyKeyStatus(), this.emptyKeyStatusCode));

        return (exchange, chain) -> {
            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            ServerHttpResponse originResponse = exchange.getResponse();
            return resolver.resolve(exchange).defaultIfEmpty(EMPTY_KEY).flatMap(key -> {
                if (EMPTY_KEY.equals(key)) {
                    if (denyEmpty) {
                        setResponseStatus(exchange, emptyKeyStatus);
                        return originResponse.setComplete();
                    }
                    return chain.filter(exchange);
                }

                return limiter.isAllowed(route.getId(), key).flatMap(response -> {
                    for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                        originResponse.getHeaders().add(header.getKey(), header.getValue());
                    }

                    if (response.isAllowed()) {
                        return chain.filter(exchange);
                    }

                    return ResultHelper.build429Result(exchange);
                });
            });
        };
    }


    private <T> T getOrDefaultValue(T configValue, T defaultValue) {
        return (configValue != null) ? configValue : defaultValue;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
