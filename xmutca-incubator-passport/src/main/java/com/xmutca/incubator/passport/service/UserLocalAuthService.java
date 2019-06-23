package com.xmutca.incubator.passport.service;

import com.xmutca.incubator.passport.model.UserLocalAuth;
import com.xmutca.incubator.passport.repository.UserLocalAuthRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-23
 */
@Service
@RequiredArgsConstructor
public class UserLocalAuthService {

    @NonNull
    private UserLocalAuthRepository authRepository;

    /**
     * 获取用户信息
     * @param key
     * @return
     */
    public UserLocalAuth get(String key) {
        return authRepository.get(key);
    }
}
