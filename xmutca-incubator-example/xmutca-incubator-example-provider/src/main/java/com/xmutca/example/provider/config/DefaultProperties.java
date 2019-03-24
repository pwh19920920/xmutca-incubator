package com.xmutca.example.provider.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author weihuang.peng
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("system")
public class DefaultProperties {

    /**
     * 名称
     */
    private String name;
}