package com.xmutca.incubator.core.web.fastjson.filter;/**
 * @author: peter
 * @version Revision: 0.0.1
 * @Date: 2018/1/22
 */

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author: peter
 * @version Revision: 0.0.1
 * @Date: 2018/1/22
 */
public class BigDecimalValueFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).toPlainString();
        }

        if (value instanceof List) {
            List list = (List) value;
            if (list.isEmpty()) {
                return value;
            }

            if (list.get(0) instanceof BigDecimal) {
                List<String> valueStr = new ArrayList<>();
                for (Object v : list) {
                    valueStr.add(((BigDecimal)v).toPlainString());
                }
                return valueStr;
            }
        }
        return value;
    }
}
