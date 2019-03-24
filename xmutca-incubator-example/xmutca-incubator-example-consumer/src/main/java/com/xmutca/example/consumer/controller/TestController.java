package com.xmutca.example.consumer.controller;

import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-18
 */
@RequiredArgsConstructor
@RestController
public class TestController {

    @NonNull
    private final RestTemplate restTemplate;

    @NonNull
    private DiscoveryClient discoveryClient;

    @RequestMapping("/consumer")
    public String test() {
        return "consumer";
    }
}
