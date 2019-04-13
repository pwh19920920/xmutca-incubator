package com.xmutca.incubator.gateway.util;

import com.alibaba.fastjson.JSON;
import com.xmutca.incubator.core.common.response.Receipt;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-13
 */
public class ResultUtils {

    private ResultUtils() {}

    /**
     * 生成报文
     * @param exchange
     * @param httpStatus
     * @param message
     * @return
     */
    public static Mono<Void> buildResult(ServerWebExchange exchange, HttpStatus httpStatus, String message) {
        ServerHttpResponse originResponse = exchange.getResponse();
        Receipt resp = new Receipt(httpStatus.value(), message);
        DataBuffer data = originResponse.bufferFactory().wrap(JSON.toJSONBytes(resp));
        setResponseStatus(exchange, httpStatus);
        return originResponse.writeWith(Flux.just(data));
    }

    /**
     * 401
     * @param exchange
     * @param httpStatus
     * @return
     */
    public static Mono<Void> build401Result(ServerWebExchange exchange, HttpStatus httpStatus) {
        return buildResult(exchange, httpStatus, "sorry, current request need login");
    }

    /**
     * 403
     * @param exchange
     * @param httpStatus
     * @return
     */
    public static Mono<Void> build403Result(ServerWebExchange exchange, HttpStatus httpStatus) {
        return buildResult(exchange, httpStatus, "forbidden request current uri");
    }

    /**
     * 429
     * @param exchange
     * @param httpStatus
     * @return
     */
    public static Mono<Void> build429Result(ServerWebExchange exchange, HttpStatus httpStatus) {
        return buildResult(exchange, httpStatus, "request to many, please wait for a moment!");
    }
}
