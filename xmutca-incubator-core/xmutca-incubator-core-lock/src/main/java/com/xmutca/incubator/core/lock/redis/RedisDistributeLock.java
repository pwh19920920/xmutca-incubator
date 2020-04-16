package com.xmutca.incubator.core.lock.redis;

import com.xmutca.incubator.core.lock.DistributeLock;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-23
 */
public class RedisDistributeLock implements DistributeLock {

    private StringRedisTemplate redisTemplate;

    public RedisDistributeLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * lock
     *
     * @param key
     * @param val
     * @return
     */
    @Override
    public boolean tryGetDistributedLock(String key, String val, long timeout, TimeUnit unit) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, val, timeout, unit);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * release
     *
     * @param key
     * @param val
     * @return
     */
    @Override
    public boolean releaseDistributedLock(String key, String val) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = redisTemplate.execute((RedisConnection connection) -> connection.eval(
                script.getBytes(), ReturnType.INTEGER, 1, key.getBytes(), val.getBytes()
        ));
        return "1L".equals(result);
    }
}
