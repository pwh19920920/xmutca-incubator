package com.xmutca.incubator.core.web.fastjson.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * fastjson long/bigdecimal to string
 *
 * @author Administrator
 * @date 2017/5/12
 */
public class LongValueFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Long) {
            return value.toString();
        }

        if (value instanceof List) {
            List list = (List) value;
            if (list.isEmpty()) {
                return value;
            }

            if (list.get(0) instanceof Long) {
                List<String> valueStr = new ArrayList<>();
                for (Object v : list) {
                    valueStr.add(v.toString());
                }
                return valueStr;
            }
        }
        return value;
    }
}
