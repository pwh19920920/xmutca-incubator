package com.xmutca.incubator.gateway.filter.authorize;

import com.xmutca.incubator.core.common.constant.RequestConstant;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.gateway.feign.SsoFeign;
import com.xmutca.incubator.gateway.util.ResultUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-13
 */
@Component
public class LoginStatusGatewayFilterFactory extends AbstractGatewayFilterFactory<LoginStatusGatewayFilterFactory.Config> implements Ordered {

    @Autowired
    private SsoFeign ssoFeign;

    public LoginStatusGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(RequestConstant.REQUEST_HEADER_TOKEN);
            Result<String> result = ssoFeign.checkAndGetUserId(token);
            if (null == result.getData()) {
                return ResultUtils.build401Result(exchange, config.getStatusCode());
            }

            // 转发用户信息
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(RequestConstant.REQUEST_HEADER_USER, result.getData()).build();
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

        /**
         * 响应状态码
         */
        private HttpStatus statusCode = HttpStatus.UNAUTHORIZED;

        public Config setStatusCode(HttpStatus statusCode) {
			this.statusCode = statusCode;
			return this;
		}
    }
}
