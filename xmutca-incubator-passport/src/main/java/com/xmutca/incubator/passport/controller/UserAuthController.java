package com.xmutca.incubator.passport.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.passport.model.UserLocalAuth;
import com.xmutca.incubator.passport.service.UserLocalAuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-23
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthController {

    @NonNull
    private UserLocalAuthService userLocalAuthService;

    /**
     * 密码登陆
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/password")
    public Result<Long> checkAndGetForUsername(String username, String password) {
        // 简单判断
        if (StringUtils.isAnyEmpty(username, password)) {
            return new Result<>(HttpStatus.BAD_REQUEST.value(), "用户名或者密码不能为空");
        }

        // 获取数据
        UserLocalAuth userLocalAuth = userLocalAuthService.get(username);
        if (null == userLocalAuth) {
            return new Result<>(HttpStatus.BAD_REQUEST.value(), "用户名或者密码错误");
        }

        // 校验一致性
        if (!encodePassword(userLocalAuth.getSalt(), password).equals(userLocalAuth.getPassword())) {
            return new Result<>(HttpStatus.BAD_REQUEST.value(), "用户名或者密码错误");
        }

        // 返回数据
        return new Result<>(userLocalAuth.getUserId());
    }

    /**
     * 密码加密
     * @param salt
     * @param password
     * @return
     */
    public String encodePassword(String salt, String password) {
        return DigestUtil.md5Hex(password + salt);
    }
}
