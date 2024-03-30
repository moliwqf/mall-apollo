package com.moli.mall.gateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.moli.mall.common.constant.AuthConstant;
import com.moli.mall.gateway.config.properties.IgnoreUrlsProperties;
import com.moli.mall.gateway.fileter.IgnoreUrlsFilter;
import com.moli.mall.gateway.handler.AccessDeniedHandler;
import com.moli.mall.gateway.handler.AuthEntryPoint;
import com.moli.mall.gateway.handler.AuthorizationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-03-29 16:37:20
 * @description 资源服务配置类 - 权限相关配置
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {

    @Resource
    private AuthEntryPoint authEntryPoint;

    @Resource
    private IgnoreUrlsFilter IgnoreUrlsFilter;

    @Resource
    private IgnoreUrlsProperties ignoreUrlsProperties;

    @Resource
    private AuthorizationManager authorizationManager;

    @Resource
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * 核心组件，相当于SpringSecurity的 WebSecurityConfigurerAdapter.configure
     * @param http 配置信息
     * @return 过滤链
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer(oAuth2 -> {
            // 自定义权限转换器
            oAuth2.jwt().jwtAuthenticationConverter(grantedAuthoritiesConverter());
            // 自定义认证失败处理器
            oAuth2.authenticationEntryPoint(authEntryPoint);
        });
        // 忽略白名单路径认证
        http.addFilterBefore(IgnoreUrlsFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsProperties.getUrls(), String.class))
                .permitAll() // 放行白名单
                .anyExchange().access(authorizationManager) // 配置鉴权管理器
                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler) // 配置访问拒绝策略
                .authenticationEntryPoint(authEntryPoint) // 配置认证失败策略
                .and().csrf().disable(); // 进制csrf
        return http.build();
    }

    /**
     * 自定义权限转换器
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
