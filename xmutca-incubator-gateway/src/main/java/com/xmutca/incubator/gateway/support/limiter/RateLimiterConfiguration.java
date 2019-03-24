package com.xmutca.incubator.gateway.support.limiter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 限流配置
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-22
 */
@Configuration
@EnableConfigurationProperties(RateLimiterGatewayFilterFactory.class)
public class RateLimiterConfiguration {

    @Bean
    @Primary
    public RateLimiterKeyResolver limiterKeyResolver() {
        return new RateLimiterKeyResolver();
    }

    @Bean
    @ConditionalOnBean({ RateLimiter.class, KeyResolver.class })
    public RequestRateLimiterGatewayFilterFactory requestRateLimiterGatewayFilterFactory(RateLimiter rateLimiter, KeyResolver resolver) {
        return new RequestRateLimiterGatewayFilterFactory(rateLimiter, resolver);
    }
}