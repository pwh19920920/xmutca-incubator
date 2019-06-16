package com.xmutca.incubator.passport.service;

import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.xmutca.incubator.passport.model.OauthClientInfo;
import com.xmutca.incubator.passport.repository.OauthClientInfoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-04-15
 */
@Service
@RequiredArgsConstructor
public class OauthClientInfoService {

    @NonNull
    private OauthClientInfoRepository oauthClientInfoRepository;

    /**
     * 查询全部
     * @return
     */
    @CachePenetrationProtect
    @Cached(expire = 3600, cacheType = CacheType.BOTH)
    @CacheRefresh(refresh = 1800, stopRefreshAfterLastAccess = 3600)
    public List<OauthClientInfo> findAll() {
        return oauthClientInfoRepository.findAll();
    }

    /**
     * 获取信息
     * @param clientId
     * @return
     */
    @CachePenetrationProtect
    @Cached(expire = 3600, cacheType = CacheType.BOTH, name = "sso:client_info:", key = "#clientId")
    @CacheRefresh(refresh = 1800, stopRefreshAfterLastAccess = 3600)
    public OauthClientInfo getByClientId(String clientId) {
        return oauthClientInfoRepository.getByClientId(clientId);
    }
}
