package com.moli.mall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author moli
 * @time 2024-03-26 17:23:36
 * @description 网关层
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallApolloGatewayApp {

    public static void main(String[] args) {
        SpringApplication.run(MallApolloGatewayApp.class, args);
    }
}
