package com.xmutca.example.consumer.controller;

import com.xmutca.example.consumer.feign.ProviderFeign;
import com.xmutca.incubator.core.common.response.Receipt;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-18
 */
@RequiredArgsConstructor
@RestController
public class TestController {

    @NonNull
    private ProviderFeign providerFeign;

    @RequestMapping("/consumer")
    public Receipt test() {
        return providerFeign.provider();
    }
}
