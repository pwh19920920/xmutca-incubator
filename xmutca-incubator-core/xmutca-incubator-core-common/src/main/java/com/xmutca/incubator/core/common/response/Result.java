package com.xmutca.incubator.core.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
@Getter
@Setter
public class Result<T> {

    /**
     * 状态
     */
    private Integer status;

    /**
     * 消息
     */
    private String message;

    /**
     * 回执
     */
    private T data;
}
