package com.moli.mall.gateway.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.moli.mall.common.constant.AuthConstant;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.gateway.config.properties.IgnoreUrlsProperties;
import com.nimbusds.jose.JWSObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-03-29 19:54:31
 * @description 认证管理器
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IgnoreUrlsProperties ignoreUrlsProperties;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        ServerHttpRequest request = context.getExchange().getRequest();
        URI uri = request.getURI();
        AntPathMatcher matcher = new AntPathMatcher();

        // 白名单直接放行
        for (String url : ignoreUrlsProperties.getUrls()) {
            if (matcher.match(url, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }

        // 跨域预检请求放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 不同客户端用户不能相互访问
        String token = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
        if (StrUtil.isEmpty(token)) {
            return Mono.just(new AuthorizationDecision(false));
        }
        // 将token解析为用户信息并放入到请求域中
        try {
            String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String user = jwsObject.getPayload().toString();
            UserDto userDto = JSONUtil.toBean(user, UserDto.class);

            if (AuthConstant.ADMIN_CLIENT_ID.equals(userDto.getClientId()) && !matcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }

            if (AuthConstant.PORTAL_CLIENT_ID.equals(userDto.getClientId()) && matcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }
        } catch (ParseException e) {
            return Mono.just(new AuthorizationDecision(false));
        }

        // 非管理端，直接放行
        if (!matcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
            return Mono.just(new AuthorizationDecision(true));
        }

        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_MAP_KEY);

        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
        List<String> authorities = new ArrayList<>();
        while (iterator.hasNext()) {
            String pattern = (String) iterator.next();
            if (matcher.match(pattern, uri.getPath())) {
                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
            }
        }

        authorities = authorities.stream().map(i -> AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());

        // 拥有权限的角色才能访问
        return authentication
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
