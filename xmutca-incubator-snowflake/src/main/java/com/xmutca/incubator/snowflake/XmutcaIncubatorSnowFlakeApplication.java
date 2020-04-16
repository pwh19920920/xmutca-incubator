package com.xmutca.incubator.snowflake;

import com.xmutca.incubator.snowflake.config.SystemConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * 雪花workId分配服务
 * @author weihuang.peng
 */
@EnableDiscoveryClient
@SpringBootApplication
@Import(SystemConfiguration.class)
public class XmutcaIncubatorSnowFlakeApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmutcaIncubatorSnowFlakeApplication.class, args);
	}
}
