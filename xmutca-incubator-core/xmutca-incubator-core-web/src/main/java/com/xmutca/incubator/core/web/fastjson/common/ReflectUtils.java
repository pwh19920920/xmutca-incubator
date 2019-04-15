package com.xmutca.incubator.core.web.fastjson.common;/**
 * @author: peter
 * @version Revision: 0.0.1
 * @Date: 2018/1/11
 */

import com.xmutca.incubator.core.logger.message.ExceptionLoggerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @version Revision: 0.0.1
 * @author: peter
 * @Date: 2018/1/11
 */
public class ReflectUtils {

    private ReflectUtils() {
    }

    public static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * 获取object中name的get方法
     *
     * @param object
     * @param name
     * @return
     */
    public static Method getMethod(Object object, String name) {
        String getName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        return method(object, getName);
    }

    /**
     * 获取object中name的get方法
     *
     * @param object
     * @param name
     * @return
     */
    public static Method method(Object object, String name) {
        try {
            return object.getClass().getMethod(name, null);
        } catch (Exception e) {
            ExceptionLoggerMessage.getInstance(e, "反射{}解析{}方法异常", object, name).debug(logger);
            return null;
        }
    }

    /**
     * 获取className中name的get方法
     *
     * @param className
     * @param name
     * @return
     */
    public static Method method(String className, String name) {
        try {
            return Class.forName(className).getMethod(name, null);
        } catch (Exception e) {
            ExceptionLoggerMessage.getInstance(e, "反射{}解析{}方法异常", className, name).debug(logger);
            return null;
        }
    }

    /**
     * 获取object中name的get方法
     *
     * @param object
     * @param name
     * @return
     */
    public static Method getDeclaredMethod(Object object, String name) {
        try {
            return object.getClass().getDeclaredMethod(name, null);
        } catch (Exception e) {
            ExceptionLoggerMessage.getInstance(e, "反射{}解析{}方法异常", object, name).debug(logger);
            return null;
        }
    }
}
