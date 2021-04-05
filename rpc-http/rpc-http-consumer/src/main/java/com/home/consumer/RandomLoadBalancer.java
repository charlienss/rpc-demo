package com.home.consumer;

import com.home.core.LoadBalancer;

import java.util.List;

/**
 * 负载均衡
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public String select(List<String> urls) {
        return urls.get(0);
    }
}