package com.xmutca.incubator.core.feign.config;

import com.alibaba.csp.sentinel.SphU;
import com.xmutca.incubator.core.feign.decode.FeignErrorDecode;
import com.xmutca.incubator.core.feign.interceptor.FeignRequestInterceptor;
import com.xmutca.incubator.core.feign.sentinel.SentinelFeign;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@Configuration
@ConditionalOnClass({ SphU.class, Feign.class })
public class FeignConfiguration {

    /**
     * 业务异常解码器
     * @return
     */
    @Bean
    public ErrorDecoder getErrorDecoder() {
        return new FeignErrorDecode();
    }

    /**
     * 请求拦截器
     * @return
     */
    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    @Primary
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    public Feign.Builder feignSentinelBuilder() {
        return SentinelFeign.builder();
    }
}
