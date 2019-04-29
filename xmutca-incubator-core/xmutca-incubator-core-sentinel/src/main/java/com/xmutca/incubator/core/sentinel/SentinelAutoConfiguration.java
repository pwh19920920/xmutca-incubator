package com.xmutca.incubator.core.sentinel;

import com.xmutca.incubator.core.sentinel.aspect.SentinelResourceAspect;
import com.xmutca.incubator.core.sentinel.config.SentinelConfiguration;
import com.xmutca.incubator.core.sentinel.config.SentinelExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-29
 */
@Configuration
@Import({SentinelConfiguration.class, SentinelResourceAspect.class, SentinelExceptionHandler.class})
public class SentinelAutoConfiguration {
}
