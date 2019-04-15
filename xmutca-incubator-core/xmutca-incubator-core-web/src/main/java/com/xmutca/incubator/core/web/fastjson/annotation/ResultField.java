package com.xmutca.incubator.core.web.fastjson.annotation;

import java.lang.annotation.*;

/**
 * Created by dhc
 * @author dhc
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResultField {

    //需要的类
    Class<?> javaType();

    /**
     * 需要返回的字段
     * @return
     */

    String[] includes() default {};

    /**
     * 需要去除的字段
     * @return
     */
    String[] excludes() default {};

    /**
     * 需要格式化字段
     * @return
     */
    FormatField[] formats() default {};

    /**
     * 数据是否需要加密
     * @return
     */
    boolean encode() default true;

}
