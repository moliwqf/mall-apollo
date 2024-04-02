package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.UmsResourceCategoryService;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.UmsResourceCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-01 15:02:20
 * @description 资源分类模块
 */
@Api(tags = "UmsResourceCategoryController", value = "资源分类模块")
@RestController
@RequestMapping("/resourceCategory")
public class UmsResourceCategoryController {

    @Resource
    private UmsResourceCategoryService umsResourceCategoryService;

    @ApiOperation("查询所有后台资源分类")
    @GetMapping("/listAll")
    public CommonResult<List<UmsResourceCategory>> listAll() {
        List<UmsResourceCategory> resourceList = umsResourceCategoryService.listAll();
        return CommonResult.success(resourceList);
    }
}
