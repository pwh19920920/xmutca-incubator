package com.xmutca.incubator.core.web.fastjson.filter;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dhc
 */
public class PropertyFilters extends PropertyPreFilters {

    private List<SerializeFilter> filters = new ArrayList<>();

    public PropertyFilters() {
        super();
    }

    @Override
    public List getFilters() {
        return filters;
    }

    @Override
    public void setFilters(List filters) {
        this.filters = filters;
    }
}
