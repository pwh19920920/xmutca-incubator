package com.xmutca.incubator.core.web.fastjson.common;

import com.xmutca.incubator.core.logger.message.ExceptionLoggerMessage;
import com.xmutca.incubator.core.web.fastjson.interceptor.BaseFormatValueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author: peter
 * @version Revision: 0.0.1
 * @Date: 2018/1/11
 */
public class FormatUtils {

    private FormatUtils() {}

    public static final Logger logger = LoggerFactory.getLogger(FormatUtils.class);

    /**
     * 银行卡卡号设置****
     */
    public static String replaceBankCard(String bankCard) {
        return replaceToIndex(bankCard, 4, bankCard.length() - 4, '*');
    }

    /**
     * 手机号设置****
     */
    public static String replaceMobile(String mobile) {
        return replaceToIndex(mobile, 3, mobile.length() - 4, '*');
    }

    /**
     * 根据下标进行替换
     *
     * @param source
     * @param beginIndex
     * @param endIndex
     * @param replace
     * @return
     */
    public static String replaceToIndex(String source, int beginIndex, int endIndex, char replace) {
        StringBuilder result = new StringBuilder(source.substring(0, beginIndex));
        for (int i = endIndex - beginIndex; i > 0; i--) {
            result.append(replace);
        }
        result.append(source.substring(endIndex));
        return result.toString();
    }

    /**
     * 获取filter
     *
     * @param filterClass
     * @param value
     * @return
     */
    public static String getFormatValue(Class<? extends BaseFormatValueFilter> filterClass, String value) {
        try {
            return filterClass.newInstance().replace(value);
        } catch (InstantiationException | IllegalAccessException e) {
            ExceptionLoggerMessage.getInstance(e, "值{}转换失败", value).error(logger);
            return value;
        }
    }
}
