package com.xmutca.incubator.sso.service;

import com.xmutca.incubator.sso.model.ClientInfo;
import com.xmutca.incubator.sso.repository.ClientInfoRepository;
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
public class ClientInfoService {

    @NonNull
    private ClientInfoRepository clientInfoRepository;

    /**
     * 查询全部
     * @return
     */
    public List<ClientInfo> selectAll() {
        return clientInfoRepository.selectList(null);
    }
}
