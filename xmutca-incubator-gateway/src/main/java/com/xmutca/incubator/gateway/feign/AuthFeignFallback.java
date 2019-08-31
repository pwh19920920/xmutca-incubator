package com.xmutca.incubator.gateway.feign;

import com.xmutca.incubator.core.common.response.Results;
import com.xmutca.incubator.gateway.dto.GatewayRouteDefinitionDto;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@Component
public class AuthFeignFallback implements AuthFeign {

    @Override
    public Results<GatewayRouteDefinitionDto> listForTimeAfter(long time) {
        return new Results<>();
    }
}
