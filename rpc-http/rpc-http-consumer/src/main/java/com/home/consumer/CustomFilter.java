package com.home.consumer;

import com.home.core.Filter;
import com.home.core.entity.RpcRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义的过滤器
 */
@Slf4j
public class CustomFilter implements Filter {
    @Override
    public boolean filter(RpcRequest request) {
        log.info("filter {} -> {}", this.getClass().getName(), request.toString());
        return true;
    }
}