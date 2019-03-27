package com.xmutca.incubator.core.webflux.config;

import com.xmutca.incubator.core.common.exception.BaseException;
import com.xmutca.incubator.core.common.response.Receipt;
import com.xmutca.incubator.core.common.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.reactive.function.server.ServerRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@RestControllerAdvice
public class DefaultExceptionHandler {

    /**
     * 系统业务异常，包含熔断异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    public Receipt handleBaseException(BaseException ex, ServerRequest request) {
        Receipt result = ex.getExceptionResult();
        request.exchange().getResponse().setStatusCode(HttpStatus.valueOf(result.getStatus()));
        return result;
    }

    /**
     * 系统异常处理
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public Receipt handleException(Exception ex) {
        return new Receipt(HttpStatus.INTERNAL_SERVER_ERROR.value(), "sorry, unknown error has occurred!");
    }

    /**
     * 文件上传不符合要求
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MultipartException.class})
    public Receipt handleMultipartException(MultipartException ex) {
        return new Receipt(HttpStatus.BAD_REQUEST.value(), "文件不符合要求");
    }

    /**
     * 访问路径被篡改
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public Receipt handleNumberFormatException(MethodArgumentTypeMismatchException ex) {
        return new Receipt(HttpStatus.BAD_REQUEST.value(), "您当前访问路径已被篡改,请填写正确路径");
    }

    /**
     * 错误的数字格式
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public Receipt handleNumberFormatException(NumberFormatException ex) {
        return new Receipt(HttpStatus.BAD_REQUEST.value(), "请求有误");
    }

    /**
     * 请求数据体为空
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public Receipt handleAccessDeniedException(HttpMessageNotReadableException ex) {
        return new Receipt(HttpStatus.BAD_REQUEST.value(), "数据体不能为空");
    }

    /**
     * 校验异常
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
        StringBuilder buffer = new StringBuilder();
        for (ConstraintViolation<?> v : set) {
            buffer.append(v.getMessage()).append(";");
        }

        return new Receipt(HttpStatus.BAD_REQUEST.value(), buffer.toString());
    }

    /**
     * 校验异常
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public Result handleBindException(BindException ex) {
        return handleBaseBindAndMethodArgumentNotValidMessage(ex.getAllErrors());
    }

    /**
     * 校验异常
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Receipt handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return handleBaseBindAndMethodArgumentNotValidMessage(ex.getBindingResult().getAllErrors());
    }

    /**
     * 处理校验数据
     * @param allErrors
     * @return
     */
    protected Receipt handleBaseBindAndMethodArgumentNotValidMessage(List<ObjectError> allErrors) {
        StringBuffer buffer = new StringBuffer();
        for (ObjectError error : allErrors) {
            buffer.append(error.getDefaultMessage()).append(";");
        }

        return new Receipt(HttpStatus.BAD_REQUEST.value(), buffer.toString());
    }
}
