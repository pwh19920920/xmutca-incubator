package com.xmutca.incubator.core.common.exception;

/**
 * 业务异常
 * @author Peter
 */
public class ServiceException extends BaseException {

    private Integer status = 400;

    public ServiceException(String message) {
        super(message);
    }

    @Override
    public Integer getStatus() {
        return status;
    }
}
