package com.xmutca.incubator.core.webflux;

import com.xmutca.incubator.core.webflux.config.DefaultExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-03-25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DefaultExceptionHandler.class})
public @interface EnableWebFlux {
}
