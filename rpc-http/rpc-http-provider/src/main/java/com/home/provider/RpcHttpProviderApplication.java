package com.home.provider;

import com.home.api.ServiceProviderDesc;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

@Slf4j
@SpringBootApplication
public class RpcHttpProviderApplication {


    public static void main(String[] args) throws Exception {
        // 启动前进行初始化
        init();
        SpringApplication.run(RpcHttpProviderApplication.class, args);
    }

    private static void init() throws Exception {
        // zookeeper 新建节点
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder().
                connectString("localhost:2181").
                namespace("rpc-http").
                retryPolicy(retryPolicy)
                .build();
        client.start();

        // 建立用户服务的节点 全限定名的方式 com.home.api.UserService
        String userServiceName = "com.home.api.UserService";
        registerService(client, userServiceName);

    }

    /**
     * 向zookeeper注册相应的服务节点
     *
     * @param client
     * @param serviceName
     */
    private static void registerService(CuratorFramework client, String serviceName) throws Exception {

        // 用户提供者相关定义
        ServiceProviderDesc userServiceProviderDesc = ServiceProviderDesc.builder()
                .host(InetAddress.getLocalHost().getHostAddress())
                .port(8081)
                .serviceClass(serviceName)
                .build();

        // 创建节点
        try {
            if (null == client.checkExists().forPath("/" + serviceName)) {
                client.create().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName, "service".getBytes());
            }
        } catch (Exception e) {
            log.error("创建提供者节点异常:{}", e.getMessage());
        }


        // 创建服务的临时节点
        client.create().withMode(CreateMode.EPHEMERAL).
                forPath( "/" + serviceName + "/" + userServiceProviderDesc.getHost() + "_" + userServiceProviderDesc.getPort(), "provider".getBytes());

    }


}
