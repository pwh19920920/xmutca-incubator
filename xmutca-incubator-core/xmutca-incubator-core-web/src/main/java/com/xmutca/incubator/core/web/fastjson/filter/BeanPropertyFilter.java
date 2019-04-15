package com.xmutca.incubator.core.web.fastjson.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.xmutca.incubator.core.web.fastjson.annotation.FormatField;
import com.xmutca.incubator.core.web.fastjson.common.FormatUtils;
import com.xmutca.incubator.core.web.fastjson.common.ReflectUtils;
import org.springframework.boot.actuate.health.Status;

import java.lang.reflect.Method;


/**
 * @author Peter
 */
public class BeanPropertyFilter implements ValueFilter {

    @Override
    public Object process(Object obj, String name, Object value) {
        if (obj instanceof JSON || !(value instanceof String) || value instanceof Status) {
            return value;
        }
        Object replace = getReplace(obj, name, value);
        if (replace != null) {
            return replace;
        }
        return value;
    }

    public static Object getReplace(Object obj, String name, Object value) {
        Method method = ReflectUtils.getMethod(obj, name);
        if (method == null) {
            return null;
        }
        FormatField annotation = method.getAnnotation(FormatField.class);
        if (annotation == null || !(value instanceof String)) {
            return null;
        }
        Class filter = annotation.formatValueFilter();
        return FormatUtils.getFormatValue(filter, (String) value);
    }
}
