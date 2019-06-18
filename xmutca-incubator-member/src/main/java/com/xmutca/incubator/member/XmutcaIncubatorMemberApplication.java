package com.xmutca.incubator.member;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author weihuang.peng
 */
@EnableMethodCache(basePackages = "com.xmutca")
@EnableCreateCacheAnnotation
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class XmutcaIncubatorMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorMemberApplication.class, args);
	}
}
