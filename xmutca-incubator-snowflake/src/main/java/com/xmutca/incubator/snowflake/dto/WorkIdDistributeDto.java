package com.xmutca.incubator.snowflake.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020-04-15
 */
@Getter
@Setter
public class WorkIdDistributeDto {

    /**
     * workId分配
     */
    private String workId;

    /**
     * 时间戳
     */
    private Long timestamp;
}
