package com.xmutca.incubator.core.web.fastjson.interceptor;

/**
 * @author Peter
 */
public interface BaseFormatValueFilter {

    /**
     * 结果转换器
     * @param value
     * @return
     */
    default String replace(String value) {
        return value;
    }
}
