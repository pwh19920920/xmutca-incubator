package com.xmutca.incubator.core.sequence.snowflake;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 雪花算法
 * @author pengweihuang
 */
@Getter
@Setter
@ConfigurationProperties("system.generator.snowflake")
public class SnowflakeGeneratorProperties {

    private final static long MAX_WORKER_DATA_CENTER = 31;

    private final static long MIN_WORKER_DATA_CENTER = 0;

    private long workerId;

    private long dataCenterId;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        if (workerId <= MAX_WORKER_DATA_CENTER && workerId >= MIN_WORKER_DATA_CENTER) {
            this.workerId = workerId;
        }
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        if (dataCenterId <= MAX_WORKER_DATA_CENTER && dataCenterId >= MIN_WORKER_DATA_CENTER) {
            this.dataCenterId = dataCenterId;
        }
    }
}