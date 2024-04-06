package com.moli.mall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author moli
 * @time 2024-03-29 20:34:18
 * @description 认证服务中心
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallApolloAuthApp {

    public static void main(String[] args) {
        SpringApplication.run(MallApolloAuthApp.class, args);
    }
}
