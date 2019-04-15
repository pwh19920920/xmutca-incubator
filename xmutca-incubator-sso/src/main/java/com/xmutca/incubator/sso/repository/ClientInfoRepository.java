package com.xmutca.incubator.sso.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmutca.incubator.sso.model.ClientInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Mapper
public interface ClientInfoRepository extends BaseMapper<ClientInfo> {
}
