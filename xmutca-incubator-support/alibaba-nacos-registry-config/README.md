https://github.com/nacos-group/nacos-docker

直接使用docker-compose -f example/cluster-hostname.yaml up -d启动

本项目在作者的基础上，加入haproxy代理
```
haproxy:
  container_name: haproxy
  image: haproxy
  volumes:
    - ./haproxy/haproxy.cfg:/usr/local/etc/haproxy/haproxy.cfg:ro
  ports:
    - "8888:8888"
  depends_on:
    - nacos1
    - nacos2
    - nacos3
  restart: on-failure
```