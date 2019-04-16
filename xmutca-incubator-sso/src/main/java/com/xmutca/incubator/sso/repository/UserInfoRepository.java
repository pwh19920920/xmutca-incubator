package com.xmutca.incubator.sso.repository;

import com.xmutca.incubator.sso.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-16
 */
@Mapper
public interface UserInfoRepository {

    /**
     * 用户名获取用户
     * @param username
     * @return
     */
    UserInfo getByUsername(String username);
}
