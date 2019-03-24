package com.xmutca.example.consumer.feign;

import com.xmutca.incubator.core.common.response.Receipt;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-24
 */
@Component
public class ProviderFeignFallback implements ProviderFeign {

    @Override
    public Receipt provider() {
        return new Receipt("fail");
    }
}
