package com.xmutca.incubator.core.logger.message;

import lombok.Builder;
import lombok.Getter;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author: weihuang.peng
 * @version Revision: 0.0.1
 * @Date: 2017/10/24
 */
@Getter
@Builder
public class ExceptionLoggerMessage extends BaseLoggerMessage {

    /**
     * 异常类型
     */
    private String exception;

    /**
     * 异常消息
     */
    private String message;

    /**
     * 异常堆栈
     */
    private String stackTrace;

    @Override
    public LoggerType getType() {
        return LoggerType.TYPE_EXCEPTION;
    }

    /**
     * 获取实例
     * @param ex
     * @param format
     * @param args
     * @return
     */
    public static ExceptionLoggerMessage getInstance(Exception ex, String format, Object... args) {
        if (null == ex) {
            ExceptionLoggerMessage log = ExceptionLoggerMessage.builder().message(format).build();
            log.setArgs(args);
            return log;
        }

        ExceptionLoggerMessage log = ExceptionLoggerMessage
                .builder()
                .exception(ex.getClass().getSimpleName())
                .message(format)
                .stackTrace(printStackTraceToString(ex.getCause()))
                .build();
        log.setArgs(args);
        return log;
    }

    /**
     * 获取异常栈
     * @param t
     * @return
     */
    public static String printStackTraceToString(Throwable t) {
        if (null == t) {
            return StringUtils.EMPTY;
        }
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
}