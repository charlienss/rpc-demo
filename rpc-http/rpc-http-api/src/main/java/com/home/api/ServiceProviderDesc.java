package com.home.api;

import lombok.Builder;
import lombok.Data;

/**
 * 服务的相关描述
 */
@Data
@Builder
public class ServiceProviderDesc {

    /**
     * host->IP
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 服务的类名
     */
    private String serviceClass;

}
