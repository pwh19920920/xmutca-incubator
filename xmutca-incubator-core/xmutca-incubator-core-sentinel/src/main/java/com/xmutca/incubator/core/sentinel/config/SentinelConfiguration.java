package com.xmutca.incubator.core.sentinel.config;

import com.alibaba.csp.sentinel.SphU;
import com.xmutca.incubator.core.sentinel.feign.SentinelFeign;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-29
 */
@Configuration
@ConditionalOnClass({ SphU.class, Feign.class })
public class SentinelConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    public Feign.Builder feignSentinelBuilder() {
        return SentinelFeign.builder();
    }
}
