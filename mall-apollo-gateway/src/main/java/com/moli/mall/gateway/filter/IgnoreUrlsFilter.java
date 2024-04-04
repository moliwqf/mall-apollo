package com.moli.mall.gateway.filter;

import com.moli.mall.common.constant.AuthConstant;
import com.moli.mall.gateway.config.properties.IgnoreUrlsProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * @author moli
 * @time 2024-03-29 17:24:17
 * @description 白名单移除jwt请求头
 */
@SuppressWarnings("NullableProblems")
@Component
public class IgnoreUrlsFilter implements WebFilter {

    @Resource
    private IgnoreUrlsProperties ignoreUrlsProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        PathMatcher antPathMatcher = new AntPathMatcher();
        List<String> urls = ignoreUrlsProperties.getUrls();
        for (String url : urls) {
            if (antPathMatcher.match(url, uri.getPath())) {
                ServerHttpRequest newRequest = request.mutate().header(AuthConstant.JWT_TOKEN_HEADER, "").build();
                exchange = exchange.mutate().request(newRequest).build();
                return chain.filter(exchange);
            }
        }
        return chain.filter(exchange);
    }
}
