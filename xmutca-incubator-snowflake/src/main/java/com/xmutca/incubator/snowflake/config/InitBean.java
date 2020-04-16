package com.xmutca.incubator.snowflake.config;

import com.alibaba.fastjson.JSON;
import com.xmutca.incubator.core.sequence.facade.Generator;
import com.xmutca.incubator.core.sequence.snowflake.SnowflakeGeneratorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitBean implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(InitBean.class);

    @Autowired
    private SnowflakeGeneratorProperties snowflakeGeneratorProperties;

    @Override
    public void afterPropertiesSet() throws Exception {

        logger.info(" init bean..{}", JSON.toJSONString(snowflakeGeneratorProperties));
        Thread.sleep(10000);
    }
}