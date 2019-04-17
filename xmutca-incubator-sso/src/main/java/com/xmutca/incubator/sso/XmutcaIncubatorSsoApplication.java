package com.xmutca.incubator.sso;

import com.xmutca.incubator.sso.config.SystemProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author weihuang.peng
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(SystemProperties.class)
public class XmutcaIncubatorSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorSsoApplication.class, args);
	}
}
