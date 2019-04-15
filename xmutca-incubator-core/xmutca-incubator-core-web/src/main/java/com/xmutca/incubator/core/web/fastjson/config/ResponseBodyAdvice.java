package com.xmutca.incubator.core.web.fastjson.config;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import com.xmutca.incubator.core.web.fastjson.annotation.ResultField;
import com.xmutca.incubator.core.web.fastjson.annotation.SerializedField;
import com.xmutca.incubator.core.web.fastjson.filter.FormatPropertyFilter;
import com.xmutca.incubator.core.web.fastjson.filter.PropertyFilters;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jeff
 * @date 15/10/23
 */
@Configuration
@RestControllerAdvice
public class ResponseBodyAdvice extends FastJsonViewResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return FastJsonHttpMessageConverter.class.isAssignableFrom(converterType) && returnType.hasMethodAnnotation(SerializedField.class);
    }

    @Override
    protected void beforeBodyWriteInternal(FastJsonContainer container, MediaType contentType, MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {
            SerializedField serializedField = returnType.getMethodAnnotation(SerializedField.class);
            ResultField[] resultFieldList = serializedField.result();
            List<SerializeFilter> serializeFilter = new ArrayList<>();
            for (ResultField resultField : resultFieldList) {
                serializeFilter.add(new FormatPropertyFilter(resultField));
            }
            PropertyFilters filters = new PropertyFilters();
            filters.setFilters(serializeFilter);
            container.setFilters(filters);
    }
}
