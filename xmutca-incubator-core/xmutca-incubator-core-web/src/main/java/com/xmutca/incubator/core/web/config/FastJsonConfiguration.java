package com.xmutca.incubator.core.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xmutca.incubator.core.web.fastjson.config.ResponseBodyAdvice;
import com.xmutca.incubator.core.web.fastjson.filter.BeanPropertyFilter;
import com.xmutca.incubator.core.web.fastjson.filter.BigDecimalValueFilter;
import com.xmutca.incubator.core.web.fastjson.filter.LongValueFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Configuration
@Import(ResponseBodyAdvice.class)
public class FastJsonConfiguration {

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        //1、先定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastjson的配置信息，比如是否要格式化返回的json数据；
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                // 字符类型字段如果为null,输出为”“,而非null
                SerializerFeature.WriteNullStringAsEmpty,

                // List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,

                //去掉fastjson循环引用时的$
                SerializerFeature.DisableCircularReferenceDetect
        );

        // Long类型超出js的数字长度，因此需要转成字符串进行处理
        fastJsonConfig.setSerializeFilters(new LongValueFilter(), new BeanPropertyFilter(), new BigDecimalValueFilter());

        //附加：处理中文乱码（后期添加）
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return fastConverter;
    }
}


