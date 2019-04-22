package com.xmutca.incubator.core.web;

import com.xmutca.incubator.core.web.config.DefaultExceptionHandler;
import com.xmutca.incubator.core.web.config.ErrorPagesConfiguration;
import com.xmutca.incubator.core.web.config.FastJsonConfiguration;
import com.xmutca.incubator.core.web.config.RequestLoggerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@Configuration
@Import({DefaultExceptionHandler.class, ErrorPagesConfiguration.class, RequestLoggerConfiguration.class, FastJsonConfiguration.class})
public class WebAutoConfiguration {
}
