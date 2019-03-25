package com.xmutca.incubator.core.feign.decode;

import com.alibaba.fastjson.JSON;
import com.xmutca.incubator.core.common.exception.BaseException;
import com.xmutca.incubator.core.common.response.Result;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

/**
 * @author
 */
public class FeignErrorDecode implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // 这一部分的异常将会变成子系统的异常, 不会进入hystrix的fallback方法，将会进入ErrorFilter的过滤链路
        if (response.status() >= HttpStatus.BAD_REQUEST.value() && response.status() < HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            try {
                Result receipt = JSON.parseObject(Util.toString(response.body().asReader()), Result.class);
                return BaseException.getInstance(receipt);
            } catch (Exception e) {
                // TODO FeignErrorDecode业务异常反解失败
            }
        }

        // 这一部分会进入fallback
        return feign.FeignException.errorStatus(methodKey, response);
    }
}