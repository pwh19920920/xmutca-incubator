1. 如果只是单纯的想用熔断保护功能，只需开启以下配置
feign:
  hystrix:
    enabled: false
  sentinel:
    enabled: true
    
2. 如果想接入控制台, 加入了动态配置
spring:
  application:
    name: xmutca-incubator-example-consumer
  cloud:
    nacos:
      config:
        server_addr: localhost:8848
        file_extension: yml
    sentinel:
      eager: true
      enabled: true
      transport:
        dashboard: localhost:8080
        port: 8789
      datasource:
        flow:
          nacos:
            rule_type: FLOW
            server_addr: ${spring.cloud.nacos.config.server_addr}
            data_id: ${spring.application.name}_flow
        degrade:
          nacos:
            rule_type: DEGRADE
            server_addr: ${spring.cloud.nacos.config.server_addr}
            data_id: ${spring.application.name}_degrade
        param_flow:
          nacos:
            rule_type: PARAM_FLOW
            server_addr: ${spring.cloud.nacos.config.server_addr}
            data_id: ${spring.application.name}_param_flow
        system:
          nacos:
            rule_type: SYSTEM
            server_addr: ${spring.cloud.nacos.config.server_addr}
            data_id: ${spring.application.name}_system
        authority:
          nacos:
            rule_type: AUTHORITY
            server_addr: ${spring.cloud.nacos.config.server_addr}
            data_id: ${spring.application.name}_authority
      filter:
        enabled: false