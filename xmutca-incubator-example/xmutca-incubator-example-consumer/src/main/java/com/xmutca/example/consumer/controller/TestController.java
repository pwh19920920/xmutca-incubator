package com.xmutca.example.consumer.controller;

import com.xmutca.example.consumer.feign.ProviderFeign;
import com.xmutca.incubator.core.common.response.Receipt;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-18
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {

    @NonNull
    private ProviderFeign providerFeign;

    @RequestMapping("/test")
    public Receipt test() {
        log.info("consumer test request");
        return providerFeign.provider();
    }

    @RequestMapping("/consumer")
    public Receipt consumer() {
        log.info("consumer consumer request");
        return providerFeign.provider();
    }

    @RequestMapping("/test/{id}")
    public Receipt testId(@PathVariable String id, HttpServletRequest request) {
        log.info("consumer test request:{}", id);
        return new Receipt(id);
    }
}
