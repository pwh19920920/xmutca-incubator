package com.xmutca.incubator.core.common.response;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-23
 */
public class Receipt extends Result<String> {

    public Receipt(Integer status, String message) {
        super(status, message);
    }

    public Receipt(String message) {
        super(DEFAULT_STATUS, message);
    }

    public Receipt() {}
}
