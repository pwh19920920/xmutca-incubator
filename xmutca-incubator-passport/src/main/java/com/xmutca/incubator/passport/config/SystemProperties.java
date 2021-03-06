package com.xmutca.incubator.passport.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Getter
@Setter
@ConfigurationProperties("system")
public class SystemProperties {

    /**
     * sso配置
     */
    private PassportProperties passport;

    @Getter
    @Setter
    public static class PassportProperties {

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
