package com.moli.mall.admin.config;

import com.moli.mall.admin.interceptor.PageParamsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-01 09:59:49
 * @description webmvc配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private PageParamsInterceptor pageParamsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageParamsInterceptor);
    }
}
