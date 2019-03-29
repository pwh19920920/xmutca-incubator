package com.xmutca.incubator.gateway.exception;

import com.xmutca.incubator.core.common.response.Receipt;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
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
        if (error instanceof ResponseStatusException) {
            ResponseStatusException ex = ((ResponseStatusException) error);
            if (ex.getStatus() == HttpStatus.NOT_FOUND) {
                return putAttr2Result(result, ex.getStatus().value(), "sorry, page is lost!");
            }

            if (ex.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
                return putAttr2Result(result, ex.getStatus().value(), "sorry, unknown error has occurred!");
            }
        }

        if (null == error.getCause()) {
            return result;
        }

        return putAttr2Result(result);
    }

    private Map<String, Object> putAttr2Result(Map<String, Object> result) {
        return putAttr2Result(result, 500, "sorry, unknown error has occurred!");
    }

    private Map<String, Object> putAttr2Result(Map<String, Object> result, int status, String message) {
        result.put("status", status);
        result.put("message", message);
        return result;
    }
}
