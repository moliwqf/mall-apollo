package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.UmsMenuService;
import com.moli.mall.admin.vo.UmsMenuNodeVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.UmsMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author moli
 * @time 2024-04-01 14:18:46
 * @description 菜单处理器
 */
@Api(tags = "UmsMenuController", value = "菜单处理器")
@RestController
@RequestMapping("/menu")
public class UmsMenuController {

    @Resource
    private UmsMenuService umsMenuService;

    @ApiOperation("根据ID删除后台菜单")
    @PostMapping("/delete/{menuId}")
    public CommonResult<?> delete(@PathVariable Long menuId) {
        int count = umsMenuService.delete(menuId);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("修改菜单显示状态")
    @PostMapping("/updateHidden/{menuId}")
    public CommonResult<?> updateHidden(@PathVariable Long menuId,
                                        @RequestParam("hidden") Integer hidden) {
        int count = umsMenuService.updateHidden(menuId, hidden);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("修改后台菜单")
    @PostMapping("/update/{menuId}")
    public CommonResult<?> update(@PathVariable Long menuId,
                                  @RequestBody UmsMenu umsMenu) {
        int count = umsMenuService.update(menuId, umsMenu);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("添加后台菜单")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody UmsMenu umsMenu) {
        int count = umsMenuService.create(umsMenu);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("根据ID获取菜单详情")
    @GetMapping("/{menuId}")
    public CommonResult<UmsMenu> info(@PathVariable Long menuId) {
        UmsMenu umsMenu = umsMenuService.info(menuId);
        return CommonResult.success(umsMenu);
    }

    @ApiOperation("树形结构返回所有菜单列表")
    @GetMapping("/treeList")
    public CommonResult<List<UmsMenuNodeVo>> treeList() {
        List<UmsMenuNodeVo> treeList = umsMenuService.treeList();
        return CommonResult.success(treeList);
    }

    @ApiOperation("分页查询后台菜单")
    @GetMapping("/list/{parentId}")
    public CommonResult<CommonPage<UmsMenu>> list(@PathVariable Long parentId) {
        List<UmsMenu> menuList = umsMenuService.list(parentId, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(menuList));
    }
}
