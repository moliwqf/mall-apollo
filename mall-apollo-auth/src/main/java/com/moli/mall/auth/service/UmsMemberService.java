package com.moli.mall.auth.service;

import com.moli.mall.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author moli
 * @time 2024-04-06 14:50:37
 * @description 会员服务
 */
@FeignClient(value = "mall-portal", contextId = "UmsMemberService")
public interface UmsMemberService {

    @RequestMapping(value = "/sso/loadUserByUsername", method = RequestMethod.GET)
    UserDto loadUserByUsername(@RequestParam("username") String username);
}
