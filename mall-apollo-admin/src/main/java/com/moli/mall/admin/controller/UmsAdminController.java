package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.dto.UmsAdminLoginParams;
import com.moli.mall.admin.dto.UmsAdminParams;
import com.moli.mall.admin.dto.UmsAdminRoleUpdateParams;
import com.moli.mall.admin.service.UmsAdminService;
import com.moli.mall.admin.vo.UmsAdminNameIconWithMenusAndRolesVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.mbg.model.UmsAdmin;
import com.moli.mall.mbg.model.UmsRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.simpleframework.xml.Path;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
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

    @ApiOperation("删除后台用户")
    @PostMapping("/delete/{adminId}")
    public CommonResult<?> deleteAdmin(@PathVariable Long adminId) {
        int count = umsAdminService.deleteAdmin(adminId);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("后台用户登出")
    @PostMapping("/logout")
    public CommonResult<?> logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("更新指定的用户信息")
    @PostMapping("/update/{adminId}")
    public CommonResult<?> updateRoleList(@PathVariable Long adminId, @RequestBody UmsAdmin umsAdmin) {
        int count = umsAdminService.updateAdmin(adminId, umsAdmin);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("更新后台用户的角色信息")
    @PostMapping("/role/update")
    public CommonResult<?> updateRoleList(@RequestParam("adminId") Long adminId, @RequestParam("roleIds") List<Long> roleIds) {
        int count = umsAdminService.updateRoleList(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }


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

    @ApiOperation("分页获取后台用户信息")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "keyword", required = false) String keyword) {
        return umsAdminService.list(PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize(), keyword);
    }

    @ApiOperation("获取后台用户的角色列表")
    @GetMapping("/role/{adminId}")
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable("adminId") Long adminId) {
        List<UmsRole> roleList = umsAdminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("根据后台用户名获取后台用户信息")
    @GetMapping("/loadUserByUsername")
    public UserDto loadUserByUsername(@RequestParam String username) {
        return umsAdminService.loadUserByUsername(username);
    }
}
