package com.moli.mall.search.config;

import com.moli.mall.common.config.BaseSwaggerConfig;
import com.moli.mall.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author moli
 * @time 2024-04-09 15:09:50
 * @description swagger配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.moli.mall.search.controller")
                .title("mall-apollo搜索系统")
                .description("mall-apollo搜索相关接口文档")
                .contactName("moli")
                .version("1.0")
                .enableSecurity(false)
                .build();

    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }
}
