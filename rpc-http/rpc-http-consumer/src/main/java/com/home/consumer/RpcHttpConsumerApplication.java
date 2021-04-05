package com.home.consumer;

import client.RpcClient;
import com.home.api.User;
import com.home.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 服务的消费者
 */
@Slf4j
@SpringBootApplication
public class RpcHttpConsumerApplication {

    /**
     * 消费端进行消费
     */
    public static void main(String[] args) {

        UserService userService = RpcClient.create(UserService.class, "http://localhost:8081/");
        User user = userService.findById(1);
        log.info("find user id=1 from server: {}", user.getName());

//        UserService consumer = RpcClient.createFromRegistry(UserService.class,
//                "localhost:2181",
//                new TargetRouter(),
//                new RandomLoadBalancer(),
//                new CustomFilter());

    }
}
