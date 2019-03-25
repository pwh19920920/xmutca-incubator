package com.xmutca.incubator.core.logger.message;

import lombok.Builder;
import lombok.Getter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2017/10/25
 */
@Getter
@Builder
public class ServiceLoggerMessage extends BaseLoggerMessage {

    private String message;

    private String title;

    @Override
    public LoggerType getType() {
        return LoggerType.TYPE_SERVICE;
    }

    /**
     * 获取实例
     * @param format
     * @param args
     * @return
     */
    public static ServiceLoggerMessage getInstance(String title, String format, Object... args) {
        ServiceLoggerMessage log = ServiceLoggerMessage
                .builder()
                .title(title)
                .message(format)
                .build();
        log.setArgs(args);
        return log;
    }
}
