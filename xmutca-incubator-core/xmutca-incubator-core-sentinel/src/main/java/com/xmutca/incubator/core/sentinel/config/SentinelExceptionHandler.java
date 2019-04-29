package com.xmutca.incubator.core.sentinel.config;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.xmutca.incubator.core.common.response.Receipt;
import com.xmutca.incubator.core.logger.message.ExceptionLoggerMessage;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-29
 */
@Slf4j
@RestControllerAdvice
public class SentinelExceptionHandler {

    /**
     * 熔断降级
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = DegradeException.class)
    public Receipt handleDegradeException(DegradeException ex) {
        ExceptionLoggerMessage.getInstance(ex,"熔断降级", ex).error(log);
        return new Receipt(HttpStatus.INTERNAL_SERVER_ERROR.value(), "熔断降级");
    }

    /**
     * 流量控制
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(value = FlowException.class)
    public Receipt handleFlowException(FlowException ex) {
        ExceptionLoggerMessage.getInstance(ex,"流量控制", ex).error(log);
        return new Receipt(HttpStatus.TOO_MANY_REQUESTS.value(), "流量控制");
    }

    /**
     * 系统保护
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = SystemBlockException.class)
    public Receipt handleSystemBlockException(SystemBlockException ex) {
        ExceptionLoggerMessage.getInstance(ex,"系统保护", ex).error(log);
        return new Receipt(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统保护");
    }

    /**
     * 访问控制
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = AuthorityException.class)
    public Receipt handleAuthorityException(AuthorityException ex) {
        ExceptionLoggerMessage.getInstance(ex,"访问控制", ex).error(log);
        return new Receipt(HttpStatus.INTERNAL_SERVER_ERROR.value(), "访问控制");
    }

    /**
     * 热点限流
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(value = ParamFlowException.class)
    public Receipt handleParamFlowException(ParamFlowException ex) {
        ExceptionLoggerMessage.getInstance(ex,"热点限流", ex).error(log);
        return new Receipt(HttpStatus.TOO_MANY_REQUESTS.value(), "热点限流");
    }

    /**
     * 请求超时
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(value = RetryableException.class)
    public Receipt handleRetryableException(RetryableException ex) {
        ExceptionLoggerMessage.getInstance(ex,"请求超时", ex).error(log);
        return new Receipt(HttpStatus.REQUEST_TIMEOUT.value(), "sorry, request timeout!");
    }
}
