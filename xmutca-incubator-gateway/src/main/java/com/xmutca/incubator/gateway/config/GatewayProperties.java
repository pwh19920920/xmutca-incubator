package com.xmutca.incubator.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-22
 */
@Getter
@Setter
@ConfigurationProperties("system")
public class GatewayProperties {

    /**
     * jwt配置
     */
    private JwtProperties jwt;

    @Getter
    @Setter
    public static class JwtProperties {

        /**
         * 访问令牌过期时间
         */
        private Long accessTokenExpireTimeout;

        /**
         * 刷新令牌过期时间
         */
        private Long refreshTokenExpireTimeout;
    }
}
