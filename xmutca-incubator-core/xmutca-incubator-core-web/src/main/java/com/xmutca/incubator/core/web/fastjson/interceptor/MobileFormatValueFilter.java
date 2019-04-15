package com.xmutca.incubator.core.web.fastjson.interceptor;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.xmutca.incubator.core.web.fastjson.common.FormatUtils;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by dhc on 2018/7/9.
 * @author dhc
 */
public class MobileFormatValueFilter implements BaseFormatValueFilter, ObjectSerializer {
    @Override
    public String replace(String value) {
        return FormatUtils.replaceMobile(value);
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.write(FormatUtils.replaceBankCard(object.toString()));
    }
}
