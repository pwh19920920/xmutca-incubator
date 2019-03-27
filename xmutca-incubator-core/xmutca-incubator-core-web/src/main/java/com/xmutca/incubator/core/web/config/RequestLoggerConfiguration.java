package com.xmutca.incubator.core.web.config;

import com.xmutca.incubator.core.logger.message.RequestLoggerMessage;
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
    @Slf4j
    public class RequestLoggerInterceptor implements HandlerInterceptor {

        public static final String UNKNOWN = "unknown";

        @Override
        public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
            String uri = httpServletRequest.getRequestURI();
            String ip = getIpAddress(httpServletRequest);
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

        /**
         * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
         *
         * @param request
         * @return
         */
        private String getIpAddress(HttpServletRequest request) {
            // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
            String ip = request.getHeader("X-Forwarded-For");

            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }

                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }

                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }

                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }

                if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = ips[index];
                    if (!(UNKNOWN.equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
            return ip;
        }
    }
}
