package com.xmutca.incubator.core.xxl.job.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author: weihuang.peng
 * @version Revision: 0.0.1
 * @Date: 2018/4/8
 */
@RefreshScope
public class XxlJobConfiguration {

    @Autowired
    private XxlJobProperties xxlJobProperties;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setAdminAddresses(xxlJobProperties.getRegistries());
        xxlJobExecutor.setAppName(xxlJobProperties.getAppName());
        xxlJobExecutor.setIp(xxlJobProperties.getIp());
        xxlJobExecutor.setPort(xxlJobProperties.getPort());
        xxlJobExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        return xxlJobExecutor;
    }
}