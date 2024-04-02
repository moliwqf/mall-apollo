package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.UmsResourceService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.UmsResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author moli
 * @time 2024-04-01 15:07:17
 * @description 资源模块
 */
@Api(tags = "UmsResourceController", value = "资源模块")
@RestController
@RequestMapping("/resource")
public class UmsResourceController {

    @Resource
    private UmsResourceService umsResourceService;

    @ApiOperation("初始化资源角色关联数据")
    @GetMapping("/initResourceRolesMap")
    public CommonResult<?> initResourceRolesMap() {
        Map<String, List<String>> resourceRolesMap = umsResourceService.initResourceRolesMap();
        return CommonResult.success(resourceRolesMap);
    }

    @ApiOperation("根据ID删除后台资源")
    @PostMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = umsResourceService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("根据ID获取资源详情")
    @PostMapping("/{id}")
    public CommonResult<UmsResource> info(@PathVariable Long id) {
        UmsResource umsResource = umsResourceService.info(id);
        return CommonResult.success(umsResource);
    }

    @ApiOperation("修改后台资源")
    @PostMapping("/update/{id}")
    @ResponseBody
    public CommonResult<?> update(@PathVariable Long id,
                                  @RequestBody UmsResource umsResource) {
        int count = umsResourceService.update(id, umsResource);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("添加后台资源")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody UmsResource umsResource) {
        int count = umsResourceService.create(umsResource);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("分页模糊查询后台资源")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsResource>> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword) {
        List<UmsResource> resourceList = umsResourceService.list(categoryId, nameKeyword, urlKeyword, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("查询所有后台资源")
    @GetMapping("/listAll")
    public CommonResult<List<UmsResource>> listAll() {
        List<UmsResource> resourceList = umsResourceService.listAll();
        return CommonResult.success(resourceList);
    }
}
