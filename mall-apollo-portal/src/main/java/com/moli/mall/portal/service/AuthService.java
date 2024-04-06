package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author moli
 * @time 2024-04-06 14:23:39
 * @description 认证服务
 */
@FeignClient(value = "mall-auth", contextId = "AuthService")
public interface AuthService {

    /**
     * 获取token信息
     * @param params 参数
     */
    @PostMapping("/oauth/token")
    CommonResult<?> getAccessToken(@RequestParam Map<String, String> params);
}
