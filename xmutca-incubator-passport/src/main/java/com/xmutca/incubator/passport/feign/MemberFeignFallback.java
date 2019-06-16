package com.xmutca.incubator.passport.feign;

import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.passport.dto.UserInfoDto;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-16
 */
@Component
public class MemberFeignFallback implements MemberFeign {

    @Override
    public Result<UserInfoDto> getByUsernamePassword(String username, String password) {
        return new Result<>();
    }
}
