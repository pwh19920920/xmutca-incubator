package com.xmutca.incubator.core.web.fastjson.annotation;

import com.xmutca.incubator.core.web.fastjson.interceptor.BaseFormatValueFilter;

import java.lang.annotation.*;

/**
 * @author dhc
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FormatField {

    //验证字段
    String column();
    // 校验器

    Class<? extends BaseFormatValueFilter> formatValueFilter() default BaseFormatValueFilter.class;
}
