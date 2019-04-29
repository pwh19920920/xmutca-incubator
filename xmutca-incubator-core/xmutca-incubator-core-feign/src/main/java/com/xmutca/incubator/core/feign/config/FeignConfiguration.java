package com.xmutca.incubator.core.feign.config;

import com.xmutca.incubator.core.feign.decode.FeignErrorDecode;
import com.xmutca.incubator.core.feign.interceptor.FeignRequestInterceptor;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@Configuration
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
}
