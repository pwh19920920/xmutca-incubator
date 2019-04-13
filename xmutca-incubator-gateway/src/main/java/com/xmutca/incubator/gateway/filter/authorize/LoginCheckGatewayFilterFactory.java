package com.xmutca.incubator.gateway.filter.authorize;

import com.xmutca.incubator.gateway.util.ResultUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-13
 */
@Component
public class LoginCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<LoginCheckGatewayFilterFactory.Config> implements Ordered {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 从header中获取令牌
            String token = exchange.getRequest().getHeaders().getFirst(config.getLoginCheckKey());
            if (StringUtils.isBlank(token)) {
                return ResultUtils.build401Result(exchange, config.getStatusCode());
            }

            // 令牌解密失败


            // 令牌解密成功
            String userId = "";

            // 转发用户信息
            exchange.getRequest().getHeaders().add("X-Request-User", userId);
            return chain.filter(exchange);
        };
    }

    /**
     * 生成401错误报文
     * @param exchange
     * @param config
     * @param message
     * @return
     */
    public Mono<Void> buildFailedResult(ServerWebExchange exchange, LoginCheckGatewayFilterFactory.Config config, String message) {
        return ResultUtils.buildResult(exchange, config.getStatusCode(), message);
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

        public static final String DEFAULT_LOGIN_CHECK_KEY = "X-Request-Token";

        /**
         * 请求头
         */
        private String loginCheckKey;

        /**
         * 响应状态码
         */
        private HttpStatus statusCode = HttpStatus.UNAUTHORIZED;

        /**
         * 判空处理
         * @return
         */
        public String getLoginCheckKey() {
            if (StringUtils.isBlank(loginCheckKey)) {
                return DEFAULT_LOGIN_CHECK_KEY;
            }
            return loginCheckKey;
        }
    }
}
