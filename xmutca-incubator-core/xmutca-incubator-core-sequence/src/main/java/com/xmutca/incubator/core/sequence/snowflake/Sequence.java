package com.xmutca.incubator.core.sequence.snowflake;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于Twitter的Snowflake算法实现分布式高效有序ID生产黑科技(sequence)
 * 
 * <br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * <br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * <br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * <br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * <br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * <br>
 * <br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * 
 * @author lry
 */
public class Sequence {

	/** 开始时间截 */
    private final static long twepoch = 1288834974657L;

    /** 机器id所占的位数 */
	private final static long workerIdBits = 5L;

	/** 数据标识id所占的位数 */
	private final static long datacenterIdBits = 5L;

	/** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
	private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);

	/** 支持的最大数据标识id，结果是31 */
	private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	/** 序列在id中占的位数 */
	private final static long sequenceBits = 12L;

	/** 机器ID向左移12位 */
	private final static long workerIdShift = sequenceBits;

	/** 数据标识id向左移17位(12+5) */
	private final static long datacenterIdShift = sequenceBits + workerIdBits;

	/** 时间截向左移22位(5+5+12) */
	private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	/** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
	private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

	/** 时间戳差距不能大于5 */
	public static final int TIME_STAMP_GAP = 5;

	/** 工作机器ID(0~31) */
	private long workerId;

	/** 数据中心ID(0~31) */
	private long dataCenterId;

	/** 毫秒内序列(0~4095) */
	private long sequence = 0L;

	/** 上次生成ID的时间截 */
	private long lastTimestamp = -1L;

	/**
	 * @param workerId 工作ID (0~31)
	 * @param dataCenterId 数据中心ID (0~31)
	 */
	public Sequence(long workerId, long dataCenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		
		if (dataCenterId > maxDatacenterId || dataCenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		
		this.workerId = workerId;
		this.dataCenterId = dataCenterId;
	}

	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		// 闰秒
		if (timestamp < lastTimestamp) {
			long offset = lastTimestamp - timestamp;
			if (offset <= TIME_STAMP_GAP) {
				try {
					wait(offset << 1);
					timestamp = timeGen();
					if (timestamp < lastTimestamp) {
						throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
			}
		}
		
		//$NON-NLS-解决跨毫秒生成ID序列号始终为偶数的缺陷$
		/**
		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			// 毫秒内序列溢出
			if (sequence == 0) {
				// 阻塞到下一个毫秒,获得新的时间戳
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {// 时间戳改变，毫秒内序列重置
			sequence = 0L;
		}
		**/
		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
		    long old = sequence;
		    sequence = (sequence + 1) & sequenceMask;
		    // 毫秒内序列溢出
		    if (sequence == old) {
		        // 阻塞到下一个毫秒,获得新的时间戳
		        timestamp = tilNextMillis(lastTimestamp);
		    }
		} else {// 时间戳改变，毫秒内序列重置
		    sequence = ThreadLocalRandom.current().nextLong(0, 2);
		}

		// 上次生成ID的时间截
		lastTimestamp = timestamp;

		// 移位并通过或运算拼到一起组成64位的ID
		return ((timestamp - twepoch) << timestampLeftShift)
				| (dataCenterId << datacenterIdShift)
				| (workerId << workerIdShift)
				| sequence;
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * 
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		
		return timestamp;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * 
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return SystemClock.now();
	}

}