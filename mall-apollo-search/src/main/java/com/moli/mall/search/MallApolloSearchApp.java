package com.moli.mall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author moli
 * @time 2024-04-09 15:02:36
 * @description 搜索模块
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallApolloSearchApp {

    public static void main(String[] args) {
        SpringApplication.run(MallApolloSearchApp.class, args);
    }
}
