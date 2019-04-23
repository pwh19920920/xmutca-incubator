package com.xmutca.incubator.gateway.feign;

import com.xmutca.incubator.core.common.exception.ServiceException;
import com.xmutca.incubator.core.common.response.Result;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-17
 */
@Component
public class SsoFeignFallback implements SsoFeign {

    @Override
    public Result<String> checkAndGetUserId(String token) {
        throw new ServiceException("用户令牌解析失败，请稍后再试！");
    }
}
