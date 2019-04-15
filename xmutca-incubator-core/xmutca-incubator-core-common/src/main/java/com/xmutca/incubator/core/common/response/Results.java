package com.xmutca.incubator.core.common.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * 列表问题
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
@Getter
@Setter
public class Results<T> extends Result<Collection<T>> {

    /**
     * 分页对象
     */
    private BasePaging paging;

    public Results(Integer status, String message, Collection<T> data) {
        super(status, message, data);
    }

    public Results(Integer status, String message) {
        super(status, message);
    }

    public Results(String message) {
        super(message);
    }

    public Results() {}
}
