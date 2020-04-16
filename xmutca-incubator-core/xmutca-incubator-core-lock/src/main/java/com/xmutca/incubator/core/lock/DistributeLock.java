package com.xmutca.incubator.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-23
 */
public interface DistributeLock {

    /**
     * lock
     * @param key
     * @param val
     * @param timeout
     * @param unit
     * @return
     */
    boolean tryGetDistributedLock(String key, String val, long timeout, TimeUnit unit);

    /**
     * release
     * @param key
     * @param val
     * @return
     */
    boolean releaseDistributedLock(String key, String val);
}
