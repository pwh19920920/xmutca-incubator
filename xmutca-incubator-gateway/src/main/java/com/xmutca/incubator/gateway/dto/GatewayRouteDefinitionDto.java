package com.xmutca.incubator.gateway.dto;

import com.xmutca.incubator.core.common.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@Getter
@Setter
public class GatewayRouteDefinitionDto extends RouteDefinition {

    private StatusEnum status;

    public void setRouteId(String routeId) {
        setId(routeId);
    }
}
