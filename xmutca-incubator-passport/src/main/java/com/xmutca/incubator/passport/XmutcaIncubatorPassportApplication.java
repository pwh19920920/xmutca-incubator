package com.xmutca.incubator.passport;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.xmutca.incubator.passport.config.SystemProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(SystemProperties.class)
public class XmutcaIncubatorPassportApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorPassportApplication.class, args);
	}
}
