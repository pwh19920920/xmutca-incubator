package com.xmutca.incubator.gateway.feign;

import com.xmutca.incubator.core.common.response.Results;
import com.xmutca.incubator.gateway.dto.GatewayRouteDefinitionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@Component
@FeignClient(name = "xmutca-incubator-auth", fallback = AuthFeignFallback.class)
public interface AuthFeign {

    /**
     * 获取时间之后的路由信息
     * @param time
     * @return
     */
    @GetMapping(value = "/gateway/routeDefinition/listForTimeAfter")
    Results<GatewayRouteDefinitionDto> listForTimeAfter(@RequestParam("time") long time);
}
