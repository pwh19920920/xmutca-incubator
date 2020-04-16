package com.xmutca.incubator.snowflake.common;

import java.util.regex.Pattern;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020-04-15
 */
public class Constant {

    /**
     * 最大数据中心
     */
    public static final int MAX_DATA_CENTER_ID = 15;

    /**
     * 最大工作机器
     */
    public static final int MAX_WORK_ID = 127;

    /**
     * 0
     */
    public static final int ZERO = 0;

    /**
     * groupWork缓存key, 全部
     */
    public static final String CACHE_ALL_GROUP_WORK_LIST_KEY = "GROUP_ALL_WORK_LIST_%s";

    /**
     * groupWork缓存key，已用
     */
    public static final String CACHE_USE_GROUP_WORK_LIST_KEY = "GROUP_USE_WORK_LIST_%s";

    /**
     * groupWork缓存key，已用记录
     */
    public static final String CACHE_USE_RECORD_GROUP_WORK_KEY = "GROUP_USE_RECORD_WORK_LIST_%s";

    /**
     * 分布式锁key
     */
    public static final String CACHE_DISTRIBUTE_WORK_LOCK_KEY = "DISTRIBUTE_WORK_LOCK_%s";

    /**
     * 分布式锁time
     */
    public static final Integer CACHE_DISTRIBUTE_WORK_LOCK_TIME = 10000;

    /**
     * 循环次数
     */
    public static final int DISTRIBUTE_LOOP_TIMES = 10;

    /**
     * 循环睡眠时间
     */
    public static final int DISTRIBUTE_LOOK_SLEEP_TIME = 100;

    /**
     * 正则表达式
     */
    public static final Pattern IP_PATTERN = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):?(\\d{1,5})?");

    /**
     * 工作组列表初始化
     */
    public static final String[] WORK_LIST_INIT = new String[MAX_WORK_ID + 1];

    static {
        for (int i = 0; i <= MAX_WORK_ID; i++) {
            WORK_LIST_INIT[i] = String.valueOf(i);
        }
    }
}
