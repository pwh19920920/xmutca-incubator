package com.xmutca.incubator.core.web.fastjson.filter;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.xmutca.incubator.core.web.fastjson.annotation.FormatField;
import com.xmutca.incubator.core.web.fastjson.annotation.ResultField;
import com.xmutca.incubator.core.web.fastjson.common.FormatUtils;
import com.xmutca.incubator.core.web.fastjson.interceptor.BaseFormatValueFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Peter
 */
@Slf4j
public class FormatPropertyFilter implements PropertyPreFilter, ValueFilter {
    private Class<?> clazz;
    private int maxLevel;
    private Map<String, Class<? extends BaseFormatValueFilter>> formatMap;
    private Set<String> includes;
    private Set<String> excludes;

    public FormatPropertyFilter(ResultField resultField) {
        FormatField[] formatList = resultField.formats();
        String[] excludeList = resultField.excludes();
        String[] includeList = resultField.includes();
        this.clazz = resultField.javaType();
        this.maxLevel = 0;
        this.formatMap = new HashMap<>();
        this.excludes = new HashSet();
        this.includes = new HashSet();
        for (int i = 0; i < formatList.length; ++i) {
            String item = formatList[i].column();
            if (item != null) {
                this.formatMap.put(item, formatList[i].formatValueFilter());
            }
        }
        for (int i = 0; i < excludeList.length; ++i) {
            String item = excludeList[i];
            if (item != null) {
                this.excludes.add(item);
            }
        }

        for (int i = 0; i < includeList.length; ++i) {
            String item = includeList[i];
            if (item != null) {
                this.includes.add(item);
            }
        }
    }

    public FormatPropertyFilter(Class<?> clazz, Map<String, Class<? extends BaseFormatValueFilter>> formatMap) {
        this.formatMap = formatMap;
        this.excludes = new HashSet();
        this.includes = new HashSet();
        this.maxLevel = 0;
        this.clazz = clazz;
    }

    public FormatPropertyFilter(Class<?> clazz, Map<String, Class<? extends BaseFormatValueFilter>> formatMap, Set<String> excludes) {
        this.formatMap = formatMap;
        this.excludes = excludes;
        this.includes = new HashSet();
        this.maxLevel = 0;
        this.clazz = clazz;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }


    @Override
    public boolean apply(JSONSerializer serializer, Object source, String name) {
        if (source == null) {
            return true;
        } else if (this.clazz != null && !this.clazz.isInstance(source)) {
            return true;
        } else if (this.excludes.contains(name)) {
            return false;
        } else {
            if (this.maxLevel > 0) {
                int level = 0;

                for (SerialContext context = serializer.getContext(); context != null; context = context.parent) {
                    ++level;
                    if (level > this.maxLevel) {
                        return false;
                    }
                }
            }
            return this.includes.isEmpty() || this.includes.contains(name);
        }
    }

    @Override
    public Object process(Object source, String name, Object value) {
        if (value == null || formatMap.isEmpty()) {
            return value;
        }

        if (clazz != null && !clazz.isInstance(source)) {
            return value;
        }
        if (value instanceof String && formatMap.containsKey(name)) {
            if (BaseFormatValueFilter.class == formatMap.get(name)) {
                return value;
            }
            return FormatUtils.getFormatValue(formatMap.get(name), (String) value);
        }
        return value;
    }
}
