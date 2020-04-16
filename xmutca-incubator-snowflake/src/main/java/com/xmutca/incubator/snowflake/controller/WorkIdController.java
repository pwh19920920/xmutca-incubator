package com.xmutca.incubator.snowflake.controller;

import com.alibaba.fastjson.JSON;
import com.xmutca.incubator.core.common.response.Receipt;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.core.lock.DistributeLock;
import com.xmutca.incubator.snowflake.common.Constant;
import com.xmutca.incubator.snowflake.dto.WorkIdDistributeDto;
import com.xmutca.incubator.snowflake.vo.WorkIdDistributeVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020-04-15
 */
@RestController
@RequestMapping("/workId")
@RequiredArgsConstructor
public class WorkIdController {

    @NonNull
    private StringRedisTemplate stringRedisTemplate;

    @NonNull
    private DistributeLock distributeLock;

    @NonNull
    private DiscoveryClient discoveryClient;

    /**
     * 分配workId
     * @param workIdDistributeVo
     * @return
     * @throws InterruptedException
     */
    @GetMapping(value = "/distribute")
    public Result distribute(@Valid WorkIdDistributeVo workIdDistributeVo) throws InterruptedException {
        SetOperations<String, String> setOp = stringRedisTemplate.opsForSet();
        HashOperations<String, Object, Object> hashOp = stringRedisTemplate.opsForHash();

        // 判断是否已初始化, 不存在则进行初始化
        String group = String.format("%s#%s#%s", workIdDistributeVo.getGroup(), workIdDistributeVo.getServiceName(), workIdDistributeVo.getDataCenterId());
        String address = String.format("%s:%s", workIdDistributeVo.getHost(), workIdDistributeVo.getPort());

        // 校验IP是否合法
        if (!Constant.IP_PATTERN.matcher(address).matches()) {
            return new Receipt(HttpStatus.BAD_REQUEST.value(), "IP地址不合法");
        }

        // 生成key
        String lockKey = String.format(Constant.CACHE_DISTRIBUTE_WORK_LOCK_KEY, group);
        String allGroupKey = String.format(Constant.CACHE_ALL_GROUP_WORK_LIST_KEY, group);
        String useGroupKey = String.format(Constant.CACHE_USE_GROUP_WORK_LIST_KEY, group);
        String useRecordKey = String.format(Constant.CACHE_USE_RECORD_GROUP_WORK_KEY, group);

        // 如果记录存在，则说明尚未过期
        String cacheResult = (String) hashOp.get(useRecordKey, address);
        if (StringUtils.isNotBlank(cacheResult)) {
            return new Result<>(JSON.parseObject(cacheResult, WorkIdDistributeDto.class));
        }

        // 判断是否已初始化全部列表
        if (!exists(allGroupKey)) {
            // 初始化
            setOp.add(allGroupKey, Constant.WORK_LIST_INIT);
        }

        // 分布式锁，取到锁才可以出来，否者只能超时处理
        int loopTime = 0;
        while (true) {
            if (loopTime > Constant.DISTRIBUTE_LOOP_TIMES) {
                return new Receipt(HttpStatus.TOO_MANY_REQUESTS.value(), "WorkId分配请求锁并发冲突，稍后再试");
            }

            // 判断是否已注册，且尚未过期
            boolean locked = distributeLock.tryGetDistributedLock(lockKey, group, Constant.CACHE_DISTRIBUTE_WORK_LOCK_TIME, TimeUnit.MILLISECONDS);
            if (locked) {
                break;
            }

            // 熟眠会儿，避免直接退出
            Thread.sleep(Constant.DISTRIBUTE_LOOK_SLEEP_TIME);
            loopTime++;
        }

        try {
            // 在无用中取出来, 选取处理
            Set<String> unUseWorkId = setOp.difference(allGroupKey, useGroupKey);
            if (ObjectUtils.isEmpty(unUseWorkId)) {
                return new Receipt(HttpStatus.INTERNAL_SERVER_ERROR.value(), "WorkId已分配完毕");
            }

            // 获取第一个workId
            String workId = unUseWorkId.iterator().next();

            // 添加到已用记录中
            WorkIdDistributeDto dto = new WorkIdDistributeDto();
            dto.setWorkId(workId);
            dto.setTimestamp(System.currentTimeMillis());

            hashOp.putIfAbsent(useRecordKey, address, JSON.toJSONString(dto));

            // 添加到已使用的列表中
            setOp.add(useGroupKey, workId);

            // 返回数据
            return new Result<>(dto);
        } finally {
            distributeLock.releaseDistributedLock(lockKey, group);
        }
    }

    /**
     * 判断是否已初始化
     * @param group
     * @return
     */
    public Boolean exists(String group) {
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) {
                return redisConnection.exists(group.getBytes());
            }
        });
    }
}
