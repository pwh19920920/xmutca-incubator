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

    public static final long MAX_WORKER_DATA_CENTER = 15;

    public static final long MIN_WORKER_DATA_CENTER = 0;

    public static final long MAX_WORKER_ID = 63;

    public static final long MIN_WORKER_ID = 0;

    private long workerId = MIN_WORKER_ID;

    private long dataCenterId = MIN_WORKER_DATA_CENTER;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        if (workerId <= MAX_WORKER_ID && workerId >= MIN_WORKER_ID) {
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