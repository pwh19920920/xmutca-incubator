package com.xmutca.incubator.core.sequence;

import com.xmutca.incubator.core.sequence.config.GeneratorConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Configuration
@Import(GeneratorConfiguration.class)
public class SequenceAutoConfiguration {
}

