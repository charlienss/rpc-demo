package com.home.core;

import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalancer {

    String select(List<String> urls);

}
