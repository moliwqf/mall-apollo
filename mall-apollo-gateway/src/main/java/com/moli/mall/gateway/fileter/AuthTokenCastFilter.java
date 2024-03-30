package com.moli.mall.gateway.fileter;

import cn.hutool.core.util.StrUtil;
import com.moli.mall.common.constant.AuthConstant;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.List;

/**
 * @author moli
 * @time 2024-03-30 21:49:25
 * @description 认证token转换过滤器
 */
@Order(value = 0)
@Slf4j
@Component
public class AuthTokenCastFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 如果有token，就将token转化为用户信息放入到请求头中
        HttpHeaders headers = request.getHeaders();
        List<String> authorizations = headers.get(AuthConstant.JWT_TOKEN_HEADER);
        if (CollectionUtils.isEmpty(authorizations) || StrUtil.isEmpty(authorizations.get(0))) return chain.filter(exchange);
        String token = authorizations.get(0).replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        // 将token解析为用户信息并放入到请求域中
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            String user = jwsObject.getPayload().toString();
            log.info("Authorization Global Filter cast token -> user = {}", user);
            ServerHttpRequest newRequest = exchange.getRequest().mutate().header(AuthConstant.USER_TOKEN_HEADER, user).build();
            exchange = exchange.mutate().request(newRequest).build();
            return chain.filter(exchange);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
