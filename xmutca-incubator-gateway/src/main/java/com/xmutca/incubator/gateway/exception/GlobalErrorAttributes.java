package com.xmutca.incubator.gateway.exception;

import com.xmutca.incubator.core.common.exception.BaseException;
import com.xmutca.incubator.core.common.response.Receipt;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-24
 */
@Component
@RestController
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    /**
     * fallback
     *
     * @return
     */
    @GetMapping("/error/fallback")
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Mono<Receipt> fallback() {
        return Mono.just(new Receipt(HttpStatus.BAD_GATEWAY.value(), "sorry, system is fallback, please wait for a moment!"));
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> result = super.getErrorAttributes(request, includeStackTrace);
        Throwable error = getError(request);
        HttpStatus status = getHttpStatus(result);

        // 通用异常处理
        if (error instanceof BaseException) {
            BaseException ex = ((BaseException) error);
            return putAttr2Result(result, ex.getStatus(), ex.getMessage());
        }

        // 本地异常处理
        if (status == HttpStatus.NOT_FOUND) {
            return putAttr2Result(result, HttpStatus.NOT_FOUND.value(), "sorry, page is lost!");
        }

        // 默认异常处理
        return putAttr2Result(result, status.value(), status.getReasonPhrase());
    }

    /**
     * 根据code获取对应的HttpStatus
     * @param errorAttributes
     */
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int) errorAttributes.get("status");
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 异常包装
     * @param result
     * @param status
     * @param message
     * @return
     */
    private Map<String, Object> putAttr2Result(Map<String, Object> result, int status, String message) {
        result.put("status", status);
        result.put("message", message);
        return result;
    }
}
