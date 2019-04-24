package com.xmutca.incubator.gateway.util;

import com.alibaba.fastjson.JSON;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
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

    private ResultUtils() {
    }

    /**
     * 生成报文
     *
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
        originResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.JSON_UTF_8.toString());
        return originResponse.writeWith(Flux.just(data));
    }

    /**
     * 400
     *
     * @param exchange
     * @return
     */
    public static Mono<Void> build400Result(ServerWebExchange exchange) {
        return buildResult(exchange, HttpStatus.BAD_REQUEST, "sorry, current request is bad");
    }

    /**
     * 400
     * @param exchange
     * @param message
     * @return
     */
    public static Mono<Void> build400Result(ServerWebExchange exchange, String message) {
        return buildResult(exchange, HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 401
     *
     * @param exchange
     * @return
     */
    public static Mono<Void> build401Result(ServerWebExchange exchange) {
        return buildResult(exchange, HttpStatus.UNAUTHORIZED, "sorry, current request need login");
    }

    /**
     * 401
     * @param exchange
     * @param message
     * @return
     */
    public static Mono<Void> build401Result(ServerWebExchange exchange, String message) {
        return buildResult(exchange, HttpStatus.UNAUTHORIZED, message);
    }

    /**
     * 403
     *
     * @param exchange
     * @return
     */
    public static Mono<Void> build403Result(ServerWebExchange exchange) {
        return buildResult(exchange, HttpStatus.FORBIDDEN, "forbidden request current uri");
    }

    /**
     * 403
     * @param exchange
     * @param message
     * @return
     */
    public static Mono<Void> build403Result(ServerWebExchange exchange, String message) {
        return buildResult(exchange, HttpStatus.FORBIDDEN, message);
    }

    /**
     * 429
     *
     * @param exchange
     * @return
     */
    public static Mono<Void> build429Result(ServerWebExchange exchange) {
        return buildResult(exchange, HttpStatus.TOO_MANY_REQUESTS, "request to many, please wait for a moment!");
    }
}
