package com.xmutca.incubator.core.feign.decode;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.xmutca.incubator.core.common.exception.BaseException;
import com.xmutca.incubator.core.common.response.Result;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.nio.charset.Charset;

/**
 * @author
 */
@Slf4j
public class FeignErrorDecode implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // 这一部分的异常将会变成子系统的异常, 不会进入hystrix的fallback方法，将会进入ErrorFilter的过滤链路
        if (response.status() >= HttpStatus.BAD_REQUEST.value() && response.status() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            try {
                String json = Util.toString(response.body().asReader(Charset.defaultCharset()));
                Result receipt = JSON.parseObject(json, Result.class);
                BaseException ex = BaseException.getInstance(receipt);
                return new HystrixBadRequestException(ex.getMessage(), ex);
            } catch (Exception e) {
                // TODO FeignErrorDecode业务异常反解失败
            }
        }

        // 这一部分会进入fallback
        return feign.FeignException.errorStatus(methodKey, response);
    }
}