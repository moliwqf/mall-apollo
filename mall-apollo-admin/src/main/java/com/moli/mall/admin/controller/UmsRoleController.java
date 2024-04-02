package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.UmsRoleService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.UmsMenu;
import com.moli.mall.mbg.model.UmsResource;
import com.moli.mall.mbg.model.UmsRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-03-31 16:09:43
 * @description 后台用户角色处理器
 */
@Api(tags = "UmsRoleController", value = "后台用户角色处理器")
@RestController
@RequestMapping("/role")
public class UmsRoleController {

    @Resource
    private UmsRoleService umsRoleService;

    @ApiOperation("修改角色状态")
    @PostMapping("/updateStatus/{roleId}")
    public CommonResult<?> updateStatus(@PathVariable Long roleId, @RequestParam(value = "status") Integer status) {
        UmsRole umsRole = new UmsRole();
        umsRole.setStatus(status);
        int count = umsRoleService.updateRole(roleId, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("给角色分配资源")
    @RequestMapping(value = "/allocResource", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<?> allocResource(@RequestParam("roleId") Long roleId,
                                         @RequestParam("resourceIds") List<Long> resourceIds) {
        int count = umsRoleService.allocResource(roleId, resourceIds);
        return CommonResult.success(count);
    }

    @ApiOperation("获取角色相关资源")
    @GetMapping("/listResource/{roleId}")
    @ResponseBody
    public CommonResult<List<UmsResource>> listResource(@PathVariable Long roleId) {
        List<UmsResource> roleList = umsRoleService.listResource(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("给角色分配菜单")
    @PostMapping("/allocMenu")
    public CommonResult<?> allocMenu(@RequestParam("roleId") Long roleId,
                                     @RequestParam("menuIds") List<Long> menuIds) {
        int count = umsRoleService.allocMenu(roleId, menuIds);
        return CommonResult.success(count);
    }

    @ApiOperation("获取角色相关菜单")
    @GetMapping("/listMenu/{roleId}")
    public CommonResult<List<UmsMenu>> listMenu(@PathVariable Long roleId) {
        List<UmsMenu> roleList = umsRoleService.listMenu(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("批量删除角色")
    @PostMapping("/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> roleIdList) {
        int count = umsRoleService.deleteRoles(roleIdList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("添加角色")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody UmsRole role) {
        int count = umsRoleService.createRole(role);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改角色")
    @PostMapping("/update/{roleId}")
    public CommonResult<?> updateRole(@PathVariable Long roleId, @RequestBody UmsRole role) {
        int count = umsRoleService.updateRole(roleId, role);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("分页获取角色信息")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsRole>> list(@RequestParam(value = "keyword", required = false) String keyword) {
        List<UmsRole> roleList = umsRoleService.list(PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize(), keyword);
        return CommonResult.success(CommonPage.restPage(roleList));
    }

    @ApiOperation("获取所有的角色")
    @GetMapping("/listAll")
    public CommonResult<List<UmsRole>> listAll() {
        List<UmsRole> roleList = umsRoleService.listAll();
        return CommonResult.success(roleList);
    }
}
