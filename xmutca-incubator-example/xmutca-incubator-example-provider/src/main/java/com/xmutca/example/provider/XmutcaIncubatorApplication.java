package com.xmutca.example.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author weihuang.peng
 */
@EnableDiscoveryClient
@SpringBootApplication
public class XmutcaIncubatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorApplication.class, args);
	}
}
