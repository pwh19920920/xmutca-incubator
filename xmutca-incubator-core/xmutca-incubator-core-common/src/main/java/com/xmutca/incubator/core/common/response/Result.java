package com.xmutca.incubator.core.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
@Getter
@Setter
public class Result<T> {

    public static final Integer DEFAULT_STATUS = 200;

    /**
     * 状态
     */
    private Integer status = DEFAULT_STATUS;

    /**
     * 消息
     */
    private String message;

    /**
     * 回执
     */
    private T data;

    public Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Result(Integer status, String message) {
        this(status, message, null);
    }

    public Result(String message) {
        this(DEFAULT_STATUS, message, null);
    }

    public Result() {
        this(DEFAULT_STATUS, null, null);
    }
}
