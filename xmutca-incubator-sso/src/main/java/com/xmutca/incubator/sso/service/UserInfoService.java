package com.xmutca.incubator.sso.service;

import com.xmutca.incubator.sso.model.UserInfo;
import com.xmutca.incubator.sso.repository.UserInfoRepository;
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
     * 通过用户名获取用户
     * @param username
     * @return
     */
    public UserInfo getByUsername(String username) {
        return userInfoRepository.getByUsername(username);
    }
}
