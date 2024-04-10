package com.moli.mall.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author moli
 * @time 2024-04-09 15:14:22
 * @description MyBatis 配置类
 */
@Configuration
@MapperScan({"com.moli.mall.*.mapper", "com.moli.mall.*.dao"})
public class MyBatisConfig {
}
