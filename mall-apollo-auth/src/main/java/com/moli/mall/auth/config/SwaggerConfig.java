package com.moli.mall.auth.config;

import com.moli.mall.common.config.BaseSwaggerConfig;
import com.moli.mall.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author moli
 * @time 2024-03-29 15:20:24
 * @description swagger 配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.moli.mall.auth.controller")
                .title("mall-apollo后台系统")
                .description("mall-apollo后台相关接口文档")
                .contactName("moli")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }
}
