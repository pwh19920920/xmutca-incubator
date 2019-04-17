package com.xmutca.incubator.core.sequence;

import com.xmutca.incubator.core.sequence.config.GeneratorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(GeneratorConfiguration.class)
public @interface EnableSequence {
}
