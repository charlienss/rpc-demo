package com.home.provider.controller;

import com.home.core.entity.RpcRequest;
import com.home.core.entity.RpcResponse;
import com.home.core.server.RpcInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * rpc服务的提供者
 */
@RestController
public class RpcProviderController {

    @Autowired
    private RpcInvoker rpcInvoker;

    @PostMapping("/")
    public RpcResponse invoke(@RequestBody RpcRequest request) {
        return rpcInvoker.invoke(request);
    }


}
