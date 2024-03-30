package com.moli.mall.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author moli
 * @time 2024-03-26 17:25:11
 * @description 后端应用层
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallApolloAdminApp {

    public static void main(String[] args) {
        SpringApplication.run(MallApolloAdminApp.class, args);
    }
}
