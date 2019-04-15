package com.xmutca.incubator.core.logger.message;

import lombok.Builder;
import lombok.Getter;

/**
 * request请求日志
 *
 * @author weihuang
 */
@Getter
@Builder
public class RequestLoggerMessage extends BaseLoggerMessage {

    private String method;

    private String ip;

    private String uri;

    @Override
    public LoggerType getType() {
        return LoggerType.TYPE_REQUEST;
    }

    /**
     * 获取实例
     * @return
     */
    public static RequestLoggerMessage getInstance(String ip, String method, String uri) {
        return RequestLoggerMessage
                .builder()
                .ip(ip)
                .method(method)
                .uri(uri)
                .build();
    }
}