package com.xmutca.incubator.member.controller;

import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.member.model.UserInfo;
import com.xmutca.incubator.member.service.UserInfoService;
import com.xmutca.incubator.member.vo.UserInfoVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-16
 */
@RestController
@RequestMapping("/userInfo")
@RequiredArgsConstructor
public class UserInfoController {

    @NonNull
    private UserInfoService userInfoService;

    /**
     * 通过用户名密码获取
     * @param userInfoVo
     * @return
     */
    @GetMapping("/getByUsernamePassword")
    public Result<UserInfo> getByUsernamePassword(UserInfoVo userInfoVo) {
        UserInfo userInfo = userInfoService.getByUsernamePassword(userInfoVo);
        return new Result<>(userInfo);
    }
}
