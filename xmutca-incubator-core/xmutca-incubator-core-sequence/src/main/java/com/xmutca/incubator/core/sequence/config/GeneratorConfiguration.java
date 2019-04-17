package com.xmutca.incubator.core.sequence.config;

import com.xmutca.incubator.core.sequence.facade.Generator;
import com.xmutca.incubator.core.sequence.snowflake.SnowflakeGenerator;
import com.xmutca.incubator.core.sequence.snowflake.SnowflakeGeneratorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Configuration
@EnableConfigurationProperties(SnowflakeGeneratorProperties.class)
public class GeneratorConfiguration {

    /**
     * 默认的
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "system.generator.type", havingValue = "snowflake", matchIfMissing = true)
    public Generator getGenerator(SnowflakeGeneratorProperties properties) {
        return new SnowflakeGenerator(properties);
    }
}
