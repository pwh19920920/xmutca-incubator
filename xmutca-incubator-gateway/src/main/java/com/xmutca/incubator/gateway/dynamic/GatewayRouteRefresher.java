package com.xmutca.incubator.gateway.dynamic;

import com.xmutca.incubator.core.common.enums.StatusEnum;
import com.xmutca.incubator.core.common.response.Results;
import com.xmutca.incubator.gateway.dto.GatewayRouteDefinitionDto;
import com.xmutca.incubator.gateway.feign.AuthFeign;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-08-31
 */
@Component
@RequiredArgsConstructor
public class GatewayRouteRefresher {

    public static final Logger LOGGER = LoggerFactory.getLogger(GatewayRouteRefresher.class);

    private static final AtomicLong lastTimestamp = new AtomicLong(0);

    @NonNull
    private ApplicationEventPublisher publisher;

    @NonNull
    private GatewayRouteRepository gatewayRouteRepository;

    @NonNull
    private AuthFeign authFeign;

    public void refresh() {
        Results<GatewayRouteDefinitionDto> results = authFeign.listForTimeAfter(lastTimestamp.get());
        if (null == results.getData() || results.getData().isEmpty()) {
            LOGGER.info("从auth拉去回来的路由变动信息为空，本次刷新动作忽略");
            return;
        }

        List<GatewayRouteDefinitionDto> saveRoutes = results.getData().stream().filter(result -> result.getStatus() == StatusEnum.STATUS_NORMAL).collect(Collectors.toList());
        List<GatewayRouteDefinitionDto> removeRoutes = results.getData().stream().filter(result -> result.getStatus() != StatusEnum.STATUS_NORMAL).collect(Collectors.toList());

        gatewayRouteRepository.batchSave(Flux.fromIterable(saveRoutes)).thenMany(gatewayRouteRepository.batchRemove(Flux.fromIterable(removeRoutes))).subscribe(consumer -> {
            // rpc获取lastTimestamp之后的路由信息，如果有新数据则一一进行获取
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            LOGGER.info("执行刷新逻辑，本次执行新增{}条数据，删除{}条数据", saveRoutes.size(), removeRoutes.size());
        });
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void refreshToGet() {
        refresh();
    }
}
