package com.xmutca.incubator.passport.repository;

import com.xmutca.incubator.passport.model.UserLocalAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-23
 */
@Mapper
public interface UserLocalAuthRepository {

    /**
     * 通过电话或者用户名获取
     * @param key
     * @return
     */
    UserLocalAuth get(String key);
}
