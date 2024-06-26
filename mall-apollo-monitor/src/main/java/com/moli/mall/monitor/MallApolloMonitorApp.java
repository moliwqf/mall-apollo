package com.moli.mall.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author moli
 * @time 2024-04-10 16:01:40
 * @description 监控模块
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class MallApolloMonitorApp {

    public static void main(String[] args) {
        SpringApplication.run(MallApolloMonitorApp.class, args);
    }
}
