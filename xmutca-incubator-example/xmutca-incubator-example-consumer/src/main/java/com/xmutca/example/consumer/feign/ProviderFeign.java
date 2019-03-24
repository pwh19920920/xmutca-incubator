package com.xmutca.example.consumer.feign;

import com.xmutca.incubator.core.common.response.Receipt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-24
 */
@Component
@FeignClient(name = "xmutca-incubator-example-provider", fallback = ProviderFeignFallback.class)
public interface ProviderFeign {

    /**
     * provider
     * @return
     */
    @GetMapping("/provider")
    Receipt provider();
}
