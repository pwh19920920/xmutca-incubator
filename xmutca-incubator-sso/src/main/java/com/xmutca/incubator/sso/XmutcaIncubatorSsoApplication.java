package com.xmutca.incubator.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author weihuang.peng
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class XmutcaIncubatorSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorSsoApplication.class, args);
	}
}
