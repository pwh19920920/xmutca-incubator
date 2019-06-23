package com.xmutca.incubator.passport.service;

import com.xmutca.incubator.passport.repository.UserThirdAuthRepository;
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
public class UserThirdAuthService {

    @NonNull
    private UserThirdAuthRepository authRepository;
}
