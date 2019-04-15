package com.xmutca.incubator.core.web.fastjson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dhc
 * @author dhc
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializedField {

    /**
     * 需要返回的字段
     * @return
     */
     ResultField[] result() default {};
}
