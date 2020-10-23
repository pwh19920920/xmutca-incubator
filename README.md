# xmutca-incubator

一、鉴权处理方案
1. 网关层利用gateway的断言以及过滤器进行鉴权, 对上游服务其他传递用户信息
2. 利用feign拦截器，进行用户信息的续传
3. 将当前用户模拟成session对象处理

二、整体架构方案
1. 链路追踪采用SkyWalking
2. 注册中心采用阿里巴巴的Nacos
3. 断路器采用阿里巴巴的Sentinel
4. 分布式任务调度采用xxl-job
5. 服务管理采用Spring-boot-admin
6. 服务基于Spring Cloud Greenwich.SR1
7. 分布式id采用雪花算法实现

-------
日志收集考虑使用有赞的方式操作
https://shaozi.info/p/57222793944c1f684c52d450


-------
# 分布式缓存的三大问题：缓存穿透，缓存雪崩，缓存失效
1. 缓存穿透：指的是高并发查询一个一定不存在的数据，会造成每一次请求都查询DB，解决方案：

* 使用布隆表存储所有的key
* 存储空值的key，设定一个较短的过期时间

缓存并发：有时候如果网站并发访问高，一个缓存如果失效，可能出现多个进程同时查询DB，同时设置缓存的情况，如果并发确实很大，这也可能造成DB压力过大，还有缓存频繁更新的问题。
* 根据业务场景选择以下几种方式来避免数据库宕机：限流、数据库连接池、加锁排队。

缓存失效：指的是缓存同一时间批量失效导致全落在了数据库上，关键在于集体失效，解决方案：
* 在原有的失效时间基础上增加一个随机值，比如1-5分钟随机
* 如果真的是大面积失效，我们可以根据业务场景选择以下几种方式来避免数据库宕机：限流、数据库连接池、加锁排队。


缓存雪崩：缓存雪崩可能是因为数据未加载到缓存中，或者缓存同一时间大面积的失效，从而导致所有请求都去查数据库，导致数据库CPU和内存负载过高，甚至宕机。
* 防止缓存服务器大面积GG：一般对缓存做高可用，比如REDIS的哨兵和集群。
* 根据业务场景选择以下几种方式来避免数据库宕机：限流、数据库连接池、加锁排队。
* 对于第一次上线的空缓存，我们可以使用缓存预热

缓存预热：缓存预热的目标就是在系统上线前，将数据加载到缓存中。
* 写个程序去跑。
* 单个缓存预热框架。

# fallback的几种模式

1. Fail Fast 快速失败，也就是抛异常 -》 常用
2. Fail Silent 无声失败，返回null，空list，set等等 -》 常用
3. Fallback: Static 返回默认值 -》 常用
4. Fallback: Stubbed 自己组装一个值返回 -》 常用
5. Fallback: Cache via Network 利用远程缓存
6. Primary + Secondary with Fallback 主次方式回退（主要和次要）

# gateway使用摘要
gateway中实现了oauth授权，目前已支持密码登陆login_password，刷新密钥refresh_token
1. 登陆login_password
```
curl --location --request POST 'http://localhost:8081/oauth/token' \
--header 'Content-Type: application/json' \
--data-raw '{"grant_type":"login_password", "client_id":"1", "client_secret": "1","password":"1231","username":"admin"}'
```

2. 刷新refresh_token
```
curl --location --request POST 'http://localhost:8081/oauth/token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "grant_type": "refresh_token",
    "client_id": "1",
    "client_secret": "1",
    "refresh_token": "从login_password拿到的refresh_token"
}'
```

3. 使用access_token
```
curl --location --request GET 'http://localhost:8081/consumer/test/xxx' \
--header 'authorization: bearer 从login_password或者refresh_token取回来的access_token' \
```