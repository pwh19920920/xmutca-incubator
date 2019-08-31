package com.xmutca.incubator.gateway;

import com.xmutca.incubator.gateway.config.GatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author weihuang.peng
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(GatewayProperties.class)
public class XmutcaIncubatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorApplication.class, args);
	}

}
