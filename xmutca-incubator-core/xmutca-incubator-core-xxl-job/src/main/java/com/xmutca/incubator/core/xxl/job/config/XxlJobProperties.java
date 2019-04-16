package com.xmutca.incubator.core.xxl.job.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author: weihuang.peng
 * @version Revision: 0.0.1
 * @Date: 2018/4/8
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    /**
     * 执行器AppName[选填]，为空则关闭自动注册
     */
    private String appName;

    /**
     * 执行器IP[选填]，为空则自动获取
     */
    private String ip;

    /**
     * 执行器端口号[选填]，为空则自动获取
     */
    private int port;

    /**
     * 执行器日志路径[选填]，为空则使用默认路径
     */
    private String logPath;

    /**
     * 日志保存天数[选填]，值大于3时生效
     */
    private int logRetentionDays;

    /**
     * 连接令牌
     */
    private String accessToken;

    /**
     * admin server中心地址
     */
    private String registries;
}