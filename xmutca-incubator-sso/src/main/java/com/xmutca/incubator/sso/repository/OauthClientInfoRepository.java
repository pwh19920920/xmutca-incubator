package com.xmutca.incubator.sso.repository;

import com.xmutca.incubator.sso.model.OauthClientInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Mapper
public interface OauthClientInfoRepository {

    /**
     * 查询全部
     * @return
     */
    List<OauthClientInfo> findAll();
}
