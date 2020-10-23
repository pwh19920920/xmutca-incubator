package com.xmutca.incubator.core.sequence.snowflake;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;

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

    private long workerId = 0x000000FF & getLastIPAddress();

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

    /**
     * 用IP地址最后几个字节标示
     * <p>
     * eg:192.168.1.30->30
     *
     * @return last IP
     */
    private static byte getLastIPAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] addressByte = inetAddress.getAddress();
            return addressByte[addressByte.length - 1];
        } catch (Exception e) {
            throw new RuntimeException("Unknown Host Exception", e);
        }
    }
}