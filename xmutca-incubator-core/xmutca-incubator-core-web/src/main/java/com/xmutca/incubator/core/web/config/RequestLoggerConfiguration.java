package com.xmutca.incubator.core.web.config;

import com.xmutca.incubator.core.logger.message.RequestLoggerMessage;
import com.xmutca.incubator.core.web.helper.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-27
 */
@Slf4j
@Configuration
public class RequestLoggerConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLoggerInterceptor());
    }

    /**
     * logger拦截器
     *
     * @author weihuang
     */
    public class RequestLoggerInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
            String uri = httpServletRequest.getRequestURI();
            String ip = IpUtils.getIpAddress(httpServletRequest);
            String method = httpServletRequest.getMethod();
            RequestLoggerMessage.getInstance(ip, method, uri).info(log);
            return true;
        }

        /**
         * 暂无作用
         *
         * @param httpServletRequest
         * @param httpServletResponse
         * @param o
         * @param modelAndView
         * @throws Exception
         */
        @Override
        public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
            // 暂时无用
        }

        /**
         * 暂无作用
         *
         * @param httpServletRequest
         * @param httpServletResponse
         * @param o
         * @param e
         * @throws Exception
         */
        @Override
        public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            // 暂时无用
        }
    }
}
