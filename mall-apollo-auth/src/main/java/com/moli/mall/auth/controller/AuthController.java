package com.moli.mall.auth.controller;

import com.moli.mall.auth.domain.OAuth2TokenDto;
import com.moli.mall.common.domain.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static com.moli.mall.common.constant.AuthConstant.*;

/**
 * @author moli
 * @time 2024-03-30 15:01:03
 * @description 认证处理器
 */
@Api(tags = "AuthController", value = "OAuth2认证处理中心")
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Resource
    private TokenEndpoint tokenEndpoint;

    @ApiOperation("oauth2获取token信息")
    @PostMapping("/token")
    public CommonResult<OAuth2TokenDto> postAccessToken(HttpServletRequest request,
                                                        @ApiParam("授权模式") @RequestParam(GRANT_TYPE_PARAM) String grantType,
                                                        @ApiParam("Oauth2客户端秘钥") @RequestParam(CLIENT_SECRET_PARAM) String clientSecret,
                                                        @ApiParam("Oauth2客户端ID") @RequestParam(CLIENT_ID_PARAM) String clientId,
                                                        @ApiParam("刷新token") @RequestParam(value = REFRESH_TOKEN, required = false) String refreshToken,
                                                        @ApiParam("登录用户名") @RequestParam(value = USERNAME_PARAM, required = false) String username,
                                                        @ApiParam("登录密码") @RequestParam(value = PASSWORD_PARAM, required = false) String password) throws HttpRequestMethodNotSupportedException {
        // 1. 请求参数封装
        Map<String, String> params = new HashMap<>();
        params.put(GRANT_TYPE_PARAM, grantType);
        params.put(CLIENT_SECRET_PARAM, clientSecret);
        params.put(CLIENT_ID_PARAM, clientId);
        params.putIfAbsent(REFRESH_TOKEN, refreshToken);
        params.putIfAbsent(USERNAME_PARAM, username);
        params.putIfAbsent(PASSWORD_PARAM, password);
        // 2. 获取token信息
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(request.getUserPrincipal(), params).getBody();
        // 3. 封装信息
        assert oAuth2AccessToken != null;
        OAuth2TokenDto auth2TokenDto = OAuth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .tokenHead(JWT_TOKEN_PREFIX)
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .build();

        return CommonResult.success(auth2TokenDto);
    }
}
