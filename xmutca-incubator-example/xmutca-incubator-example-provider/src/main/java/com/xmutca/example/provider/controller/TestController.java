package com.xmutca.example.provider.controller;

import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Receipt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-18
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RefreshScope
public class TestController {

    /**
     * test provider
     * @return
     */
    @RequestMapping("/provider")
    public Receipt test() {
        log.info("provider request");
        throw new ServiceException("xxx");
    }
}
