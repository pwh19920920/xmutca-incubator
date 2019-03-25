package com.xmutca.incubator.core.feign.interceptor;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (null != attributes) {
            // TODO
        }

        // feign 不支持 GET 方法传 POJO, json body转query
        if (template.method().equals(RequestMethod.GET.name()) && template.bodyTemplate() != null) {
            JsonNode node = JSON.parseObject(template.bodyTemplate(), JsonNode.class);
            Map<String, Collection<String>> queries = new HashMap<>(16);
            buildQuery(node, StringUtils.EMPTY, queries);
            template.queries(queries);
        }
    }

    /**
     * 对象转字符串
     * @param obj
     * @return
     */
    private String obj2String(Object obj) {
        if (null == obj) {
            return StringUtils.EMPTY;
        }
        return (String) obj;
    }

    /**
     * body转query
     * @param node
     * @param key
     * @param queries
     */
    private void buildQuery(JsonNode node, String key, Map<String, Collection<String>> queries) {
        if (node == null || node.isNull()) {
            return;
        }

        // 叶子节点
        if (!node.isContainerNode()) {
            queries.computeIfAbsent(key, k -> {
                Collection<String> values = new ArrayList<>();
                values.add(node.asText());
                return values;
            });
            return;
        }

        // 数组节点
        if (node.isArray()) {
            Iterator<JsonNode> it = node.elements();
            while (it.hasNext()) {
                buildQuery(it.next(), key, queries);
            }
            return;
        }

        Iterator<Map.Entry<String, JsonNode>> it = node.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> entry = it.next();
            if (StringUtils.isNotBlank(key)) {
                buildQuery(entry.getValue(), key + "." + entry.getKey(), queries);
            } else {  // 根节点
                buildQuery(entry.getValue(), entry.getKey(), queries);
            }
        }
    }
}