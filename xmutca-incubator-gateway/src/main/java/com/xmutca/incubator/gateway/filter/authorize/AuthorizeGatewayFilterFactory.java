package com.xmutca.incubator.gateway.filter.authorize;

import com.google.common.collect.Lists;
import com.xmutca.incubator.core.common.constant.RequestConstant;
import com.xmutca.incubator.gateway.util.ResultUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-13
 */
@Component
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> implements Ordered {

    public static final AntPathMatcher matcher = new AntPathMatcher();

    public AuthorizeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // 没值，直接认为不做权限处理
        if (StringUtils.isBlank(config.getAuthorizeCode()) || config.getUri().isEmpty()) {
            return (exchange, chain) -> chain.filter(exchange);
        }

        return (exchange, chain) -> {
            // 此路径无需登陆
            String user = exchange.getRequest().getHeaders().getFirst(RequestConstant.REQUEST_HEADER_USER);
            if (StringUtils.isBlank(user)) {
                return ResultUtils.build401Result(exchange);
            }

            // 获取路径判断
            String requestUri = exchange.getRequest().getPath().value();
            for (String pattern : config.getUri()) {
                if (!matcher.match(pattern, requestUri)) {
                    return ResultUtils.build403Result(exchange);
                }
            }

            // 获取用户的权限判断
            List<String> authorities = Lists.newArrayList();
            if (!authorities.contains(config.getAuthorizeCode())) {
                return ResultUtils.build403Result(exchange);
            }
            return chain.filter(exchange);
        };
    }

    @Override
    public int getOrder() {
        return 2;
    }

    /**
     * 配置对象
     */
    @Getter
    @Setter
    public static class Config {

        public static final String DEFAULT_AUTHORIZE_KEY = "X-Request-Authorize";

        /**
         * 请求地址
         */
        private List<String> uri = Lists.newArrayList();

        /**
         * 权限码
         */
        private String authorizeCode;

        /**
         * 权限键
         */
        private String authorizeKey;

        /**
         * 响应状态码
         */
        private HttpStatus statusCode = HttpStatus.FORBIDDEN;

        /**
         * 授权key为空处理
         *
         * @return
         */
        public String getAuthorizeKey() {
            if (StringUtils.isBlank(authorizeKey)) {
                authorizeKey = DEFAULT_AUTHORIZE_KEY;
            }
            return authorizeKey;
        }
    }
}
