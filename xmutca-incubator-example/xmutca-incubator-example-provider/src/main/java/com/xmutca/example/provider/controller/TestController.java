package com.xmutca.example.provider.controller;

import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Receipt;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-18
 */
@RequiredArgsConstructor
@RestController
@RefreshScope
public class TestController {

    AtomicLong atomicLong = new AtomicLong();

    /**
     * test provider
     * @return
     */
    @RequestMapping("/provider")
    public Receipt test() {
        System.out.println("provider:" + atomicLong.getAndIncrement());
        throw new ServiceException("xxx");
    }
}
