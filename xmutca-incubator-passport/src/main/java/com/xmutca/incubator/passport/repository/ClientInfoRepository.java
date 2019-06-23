package com.xmutca.incubator.passport.repository;

import com.xmutca.incubator.passport.model.ClientInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Mapper
public interface ClientInfoRepository {

    /**
     * 查询全部
     * @return
     */
    List<ClientInfo> findAll();

    /**
     * 获取信息
     * @param clientId
     * @return
     */
    ClientInfo getByClientId(String clientId);
}
