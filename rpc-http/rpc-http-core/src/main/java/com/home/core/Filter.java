package com.home.core;

import com.home.core.entity.RpcRequest;

public interface Filter {

    boolean filter(RpcRequest request);

}
