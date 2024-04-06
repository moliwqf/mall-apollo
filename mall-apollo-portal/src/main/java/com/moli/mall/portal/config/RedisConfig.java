package com.moli.mall.portal.config;

import com.moli.mall.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author moli
 * @time 2024-04-06 10:00:11
 * @description redis配置
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
