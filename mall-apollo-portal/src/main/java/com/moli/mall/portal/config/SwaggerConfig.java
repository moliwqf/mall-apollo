package com.moli.mall.portal.config;

import com.moli.mall.common.config.BaseSwaggerConfig;
import com.moli.mall.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author moli
 * @time 2024-04-06 09:59:41
 * @description SwaggerConfig
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.moli.mall.portal.controller")
                .title("mall-apollo前台系统")
                .description("mall-apollo前台相关接口文档")
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
