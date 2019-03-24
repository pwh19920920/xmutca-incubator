package com.xmutca.incubator.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * fallback
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
@RestController
public class FallbackController {

    /**
     * fallback
     * @return
     */
    @GetMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("this is fallback page");
    }
}
