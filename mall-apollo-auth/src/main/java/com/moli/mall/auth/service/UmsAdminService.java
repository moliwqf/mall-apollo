package com.moli.mall.auth.service;

import com.moli.mall.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author moli
 * @time 2024-03-30 10:23:01
 * @description 后台用户服务远程调用
 */
@FeignClient("mall-admin")
public interface UmsAdminService {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/admin/loadUserByUsername")
    UserDto loadUserByUsername(@RequestParam String username);
}
