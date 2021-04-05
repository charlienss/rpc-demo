package com.home.provider.config;

import com.home.api.UserService;
import com.home.core.entity.RpcResolver;
import com.home.core.server.RpcInvoker;
import com.home.provider.DemoResolver;
import com.home.provider.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 提供者的配置
 */
@Configurable
@Component
public class ProviderConfig {

    @Bean
    public RpcInvoker createInvoker(@Autowired RpcResolver resolver) {
        return new RpcInvoker(resolver);
    }

    @Bean
    public RpcResolver createResolver() {
        return new DemoResolver();
    }

    /**
     * 通过全限定名找到响应的接口实现
     *
     * @return
     */
    @Bean(name = "com.home.api.UserService")
    public UserService createUserService() {
        return new UserServiceImpl();
    }
}
