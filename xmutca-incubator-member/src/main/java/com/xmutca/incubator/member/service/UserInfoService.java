package com.xmutca.incubator.member.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.xmutca.incubator.member.model.UserInfo;
import com.xmutca.incubator.member.repository.UserInfoRepository;
import com.xmutca.incubator.member.vo.UserInfoVo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-16
 */
@Service
@RequiredArgsConstructor
public class UserInfoService {

    @NonNull
    private UserInfoRepository userInfoRepository;

    /**
     * 通过用户名, 密码获取用户
     * @param userInfoVo
     * @return
     */
    public UserInfo getByUsernamePassword(UserInfoVo userInfoVo) {
        UserInfo userInfo = userInfoRepository.getByUsername(userInfoVo.getUsername());
        if (!checkPassword(userInfo, userInfoVo.getPassword())) {
            return null;
        }

        return userInfo;
    }

    /**
     * 密码检查
     * @param userInfo
     * @param password
     * @return
     */
    public boolean checkPassword(UserInfo userInfo, String password) {
        if (null == userInfo) {
            return false;
        }
        return encodePassword(userInfo, password).equals(userInfo.getPassword());
    }

    /**
     * 密码加密
     * @param userInfo
     * @param password
     * @return
     */
    public String encodePassword(UserInfo userInfo, String password) {
        return DigestUtil.md5Hex(password + userInfo.getSalt());
    }
}
