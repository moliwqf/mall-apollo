package com.moli.mall.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author moli
 * @time 2024-03-29 15:43:28
 * @description MyBatis 配置类
 */
@Configuration
@MapperScan({"com.moli.mall.*.mapper","com.moli.mall.*.dao"})
public class MyBatisConfig {
}
