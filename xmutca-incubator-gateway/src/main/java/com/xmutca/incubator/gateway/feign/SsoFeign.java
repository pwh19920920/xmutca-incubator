package com.xmutca.incubator.gateway.feign;

import com.xmutca.incubator.core.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Component
@FeignClient(name = "xmutca-incubator-sso", fallback = SsoFeignFallback.class)
public interface SsoFeign {

    /**
     * 检查并获取用户ID
     * @return
     */
    @GetMapping("/oauth/checkAndGetUserId")
    Result checkAndGetUserId();
}
