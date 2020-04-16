package com.xmutca.incubator.core.xxl.job;

import com.xmutca.incubator.core.xxl.job.config.XxlJobConfiguration;
import com.xmutca.incubator.core.xxl.job.config.XxlJobProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
@Import(XxlJobConfiguration.class)
public class XxlJobAutoConfiguration {
}
