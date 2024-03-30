package com.moli.mall.admin.service;

import com.moli.mall.common.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author moli
 * @time 2024-03-30 16:29:52
 * @description 认证服务层
 */
@FeignClient("mall-auth")
public interface AuthService {

    /**
     * 获取token信息
     * @param params 参数
     */
    @PostMapping("/oauth/token")
    CommonResult<?> getAccessToken(@RequestParam Map<String, String> params);
}
