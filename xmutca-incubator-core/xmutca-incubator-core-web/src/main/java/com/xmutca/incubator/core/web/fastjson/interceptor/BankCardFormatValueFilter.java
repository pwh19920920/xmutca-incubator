package com.xmutca.incubator.core.web.fastjson.interceptor;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.xmutca.incubator.core.web.fastjson.common.FormatUtils;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Peter
 */
public class BankCardFormatValueFilter implements BaseFormatValueFilter, ObjectSerializer {

    @Override
    public String replace(String value) {
        return FormatUtils.replaceBankCard(value);
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.write(FormatUtils.replaceBankCard(object.toString()));
    }
}
