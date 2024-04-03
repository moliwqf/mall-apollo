package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.PmsProductAttributeCategoryService;
import com.moli.mall.admin.vo.PmsProductAttributeCategoryVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProductAttributeCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:55:36
 * @description 商品属性分类管理
 */
@RestController
@Api(tags = "PmsProductAttributeCategoryController", value = "商品属性分类管理")
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {

    @Resource
    private PmsProductAttributeCategoryService pmsProductAttributeCategoryService;

    @ApiOperation("删除单个商品属性分类")
    @GetMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = pmsProductAttributeCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("修改商品属性分类")
    @PostMapping("/update/{id}")
    public CommonResult<?> update(@PathVariable Long id, @RequestParam String name) {
        int count = pmsProductAttributeCategoryService.update(id, name);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("添加商品属性分类")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestParam String name) {
        int count = pmsProductAttributeCategoryService.create(name);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("获取所有商品属性分类及其下属性")
    @GetMapping("/list/withAttr")
    public CommonResult<List<PmsProductAttributeCategoryVo>> getListWithAttr() {
        List<PmsProductAttributeCategoryVo> productAttributeCategoryResultList = pmsProductAttributeCategoryService.getListWithAttr();
        return CommonResult.success(productAttributeCategoryResultList);
    }

    @ApiOperation("分页获取所有商品属性分类")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsProductAttributeCategory>> getList() {
        List<PmsProductAttributeCategory> productAttributeCategoryList = pmsProductAttributeCategoryService.getList(PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(productAttributeCategoryList));
    }

}
