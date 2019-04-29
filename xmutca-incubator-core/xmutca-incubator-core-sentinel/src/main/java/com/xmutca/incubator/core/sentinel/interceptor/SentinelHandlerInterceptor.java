package com.xmutca.incubator.core.sentinel.interceptor;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.adapter.servlet.util.FilterUtil;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.xmutca.incubator.core.common.exception.BaseException;
import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Receipt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-28
 */
public class SentinelHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String origin = parseOrigin(request);
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String uriTarget = StringUtils.defaultString(pattern, FilterUtil.filterTarget(request));
        try {
            // Clean and unify the URL.
            // For REST APIs, you have to clean the URL (e.g. `/foo/1` and `/foo/2` -> `/foo/:id`), or
            // the amount of context and resources will exceed the threshold.
            UrlCleaner urlCleaner = WebCallbackManager.getUrlCleaner();
            if (urlCleaner != null) {
                uriTarget = urlCleaner.clean(uriTarget);
            }
            RecordLog.info(String.format("[Sentinel Pre Filter] Origin: %s enter Uri Path: %s", origin, uriTarget));
            SphU.entry(uriTarget, EntryType.IN);
            return true;
        } catch (BlockException ex) {
            RecordLog.warn(String.format("[Sentinel Pre Filter] Block Exception when Origin: %s enter fall back uri: %s", origin, uriTarget), ex);
            throw new ServiceException("请求超限");
        }
    }

    /**
     * 写JSON
     *
     * @param response
     * @throws IOException
     */
    private void writeJson(HttpServletResponse response) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        Receipt receipt = new Receipt(HttpStatus.TOO_MANY_REQUESTS.value(), "请求超限");
        out.print(JSON.toJSONString(receipt));
        out.flush();
        out.close();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 业务异常，直接忽略
        if (ex instanceof BaseException) {
            throw ex;
        }

        // default ex
        Tracer.trace(ex);

        // 退出
        while (ContextUtil.getContext() != null && ContextUtil.getContext().getCurEntry() != null) {
            ContextUtil.getContext().getCurEntry().exit();
        }
        ContextUtil.exit();
    }

    /**
     * 转换目标
     *
     * @param request
     * @return
     */
    private String parseOrigin(HttpServletRequest request) {
        RequestOriginParser originParser = WebCallbackManager.getRequestOriginParser();
        String origin = EMPTY_ORIGIN;
        if (originParser != null) {
            origin = originParser.parseOrigin(request);
            if (StringUtil.isEmpty(origin)) {
                return EMPTY_ORIGIN;
            }
        }
        return origin;
    }


    private static final String EMPTY_ORIGIN = "";
}