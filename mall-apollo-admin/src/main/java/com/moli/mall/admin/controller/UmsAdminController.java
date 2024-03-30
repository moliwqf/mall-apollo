package com.moli.mall.admin.controller;

import com.moli.mall.admin.dto.UmsAdminLoginParams;
import com.moli.mall.admin.dto.UmsAdminParams;
import com.moli.mall.admin.service.UmsAdminService;
import com.moli.mall.admin.vo.UmsAdminNameIconWithMenusAndRolesVo;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.mbg.model.UmsAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-03-29 22:28:14
 * @description 后台用户模块
 */
@Api(tags = "UmsAdminController", value = "后台用户管理模块")
@RestController
@RequestMapping("/admin")
public class UmsAdminController {

    @Resource
    private UmsAdminService umsAdminService;

    @ApiOperation("后台用户登录")
    @PostMapping("/login")
    public CommonResult<?> login(@Validated @RequestBody UmsAdminLoginParams umsAdminLoginParams) {
        return umsAdminService.login(umsAdminLoginParams.getUsername(), umsAdminLoginParams.getPassword());
    }

    @ApiOperation("后台用户注册")
    @PostMapping("/register")
    public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminParams umsAdminParams) {
        UmsAdmin umsAdmin = umsAdminService.register(umsAdminParams);
        if (Objects.isNull(umsAdmin)) {
            return CommonResult.fail();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation("后台用户获取用户信息")
    @GetMapping("/info")
    public CommonResult<UmsAdminNameIconWithMenusAndRolesVo> info() {
        return umsAdminService.info();
    }

    @ApiOperation("根据后台用户名获取后台用户信息")
    @GetMapping("/loadUserByUsername")
    public UserDto loadUserByUsername(@RequestParam String username) {
        return umsAdminService.loadUserByUsername(username);
    }
}
