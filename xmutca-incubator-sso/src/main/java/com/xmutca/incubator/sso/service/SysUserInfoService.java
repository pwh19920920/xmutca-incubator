package com.xmutca.incubator.sso.service;

import com.xmutca.incubator.sso.model.SysUserInfo;
import com.xmutca.incubator.sso.repository.SysUserInfoRepository;
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
public class SysUserInfoService {

    @NonNull
    private SysUserInfoRepository sysUserInfoRepository;

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    public SysUserInfo getByUsername(String username) {
        return sysUserInfoRepository.getByUsername(username);
    }
}
