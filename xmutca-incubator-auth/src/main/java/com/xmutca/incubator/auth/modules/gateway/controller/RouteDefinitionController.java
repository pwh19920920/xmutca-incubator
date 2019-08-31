package com.xmutca.incubator.auth.modules.gateway.controller;

import com.xmutca.incubator.auth.modules.gateway.model.RouteDefinition;
import com.xmutca.incubator.auth.modules.gateway.service.RouteDefinitionService;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.core.common.response.Results;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/gateway/routeDefinition")
public class RouteDefinitionController {

    @NonNull
    private RouteDefinitionService routeDefinitionService;

    @PostMapping
    public Result<Integer> save(@RequestBody RouteDefinition routeDefinition) {
        int result = routeDefinitionService.save(routeDefinition);
        return new Result<>(result);
    }

    @GetMapping("/listForTimeAfter")
    public Results<RouteDefinition> listForTimeAfter(long time) {
        List<RouteDefinition> routeDefinitions = routeDefinitionService.listForTimeAfter(time);
        return new Results<>(routeDefinitions);
    }
}
