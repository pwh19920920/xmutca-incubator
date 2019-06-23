package com.xmutca.incubator.gateway.filter.authorize;

import com.xmutca.incubator.core.common.constant.RequestConstant;
import com.xmutca.incubator.gateway.service.TokenService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-13
 */
@Component
public class LoginStatusGatewayFilterFactory extends AbstractGatewayFilterFactory<LoginStatusGatewayFilterFactory.Config> implements Ordered {

    @Autowired
    private ApplicationContext applicationContext;

    public LoginStatusGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            TokenService tokenService = applicationContext.getBean(TokenService.class);
            String userId = tokenService.checkAndGetSubject(exchange.getRequest());

            // 转发用户信息
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(RequestConstant.REQUEST_HEADER_USER, userId).build();
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * 配置对象
     */
    @Getter
    @Setter
    public static class Config {

    }
}
