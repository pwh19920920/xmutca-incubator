package com.xmutca.example.provider.controller;

import com.xmutca.example.provider.config.DefaultProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-18
 */
@RequiredArgsConstructor
@RestController
@RefreshScope
public class TestController {

    @NonNull
    private DefaultProperties defaultProperties;

    @RequestMapping("/provider")
    public String test() {
        return "provider";
    }
}
