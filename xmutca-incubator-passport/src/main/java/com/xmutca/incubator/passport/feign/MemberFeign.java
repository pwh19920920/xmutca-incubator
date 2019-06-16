package com.xmutca.incubator.passport.feign;

import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.passport.dto.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * member系统
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-16
 */
@Component
@FeignClient(name = "xmutca-incubator-member", fallback = MemberFeignFallback.class)
public interface MemberFeign {

    /**
     * 通过用户名，密码进行获取
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @GetMapping("/userInfo/getByUsernamePassword")
    Result<UserInfoDto> getByUsernamePassword(@RequestParam("username") String username, @RequestParam("password") String password);
}
