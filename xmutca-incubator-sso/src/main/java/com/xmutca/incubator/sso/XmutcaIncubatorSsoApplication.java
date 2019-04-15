package com.xmutca.incubator.sso;

import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan("com.xmutca.incubator.sso.repository")
public class XmutcaIncubatorSsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorSsoApplication.class, args);
	}

}
