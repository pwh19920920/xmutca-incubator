package com.xmutca.incubator.passport.controller;

import com.xmutca.incubator.core.common.response.Result;
import com.xmutca.incubator.passport.model.ClientInfo;
import com.xmutca.incubator.passport.service.ClientInfoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-06-22
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientInfoController {

    @NonNull
    private ClientInfoService clientInfoService;

    /**
     * 获取客户信息
     * @param clientId
     * @return
     */
    @GetMapping("/{clientId}")
    public Result<ClientInfo> get(@PathVariable String clientId) {
        ClientInfo result = clientInfoService.getByClientId(clientId);
        return new Result<>(result);
    }
}
