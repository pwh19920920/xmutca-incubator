package com.xmutca.incubator.core.feign;

import com.xmutca.incubator.core.feign.config.FeignConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author: weihuang.peng
 * @version Revision: 0.0.1
 * @Date: 2018/4/20
 */
@Configuration
@Import({FeignConfiguration.class})
public class FeignAutoConfiguration {
}