package com.xmutca.incubator.snowflake.config;

import com.xmutca.incubator.core.lock.redis.RedisDistributeLock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Configuration
@EnableConfigurationProperties(SystemProperties.class)
public class SystemConfiguration {

    /**
     * 分布式锁
     * @param stringRedisTemplate
     * @return
     */
    @Bean
    public RedisDistributeLock redisDistributeLock(StringRedisTemplate stringRedisTemplate) {
        return new RedisDistributeLock(stringRedisTemplate);
    }
}
