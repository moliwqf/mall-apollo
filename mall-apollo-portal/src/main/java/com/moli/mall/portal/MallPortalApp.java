package com.moli.mall.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author moli
 * @time 2024-04-05 20:12:06
 * @description 前台应用
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallPortalApp {

    public static void main(String[] args) {
        SpringApplication.run(MallPortalApp.class, args);
    }
}
