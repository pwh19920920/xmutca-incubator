package com.xmutca.incubator.core.xxl.job;

import com.xmutca.incubator.core.xxl.job.config.XxlJobConfiguration;
import com.xmutca.incubator.core.xxl.job.config.XxlJobProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018/1/24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(XxlJobConfiguration.class)
@EnableConfigurationProperties(XxlJobProperties.class)
public @interface EnableXxlJob {
}
