package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.dto.PmsProductCategoryParams;
import com.moli.mall.admin.service.PmsProductCategoryService;
import com.moli.mall.admin.vo.PmsProductCategoryWithChildrenVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProductCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:14:17
 * @description 产品分类模块
 */
@Api(tags = "PmsProductCategoryController", value = "产品分类模块")
@RestController
@RequestMapping("/productCategory")
public class PmsProductCategoryController {

    @Resource
    private PmsProductCategoryService pmsProductCategoryService;

    @ApiOperation("删除商品分类")
    @PostMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = pmsProductCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("修改商品分类")
    @PostMapping("/update/{id}")
    public CommonResult<?> update(@PathVariable Long id,
                                  @Validated @RequestBody PmsProductCategoryParams productCategoryParam) {
        int count = pmsProductCategoryService.update(id, productCategoryParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("添加产品分类")
    @PostMapping("/create")
    public CommonResult<?> create(@Validated @RequestBody PmsProductCategoryParams productCategoryParam) {
        int count = pmsProductCategoryService.create(productCategoryParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("根据id获取商品分类")
    @GetMapping("/{id}")
    public CommonResult<PmsProductCategory> info(@PathVariable Long id) {
        PmsProductCategory productCategory = pmsProductCategoryService.info(id);
        return CommonResult.success(productCategory);
    }

    @ApiOperation("分页查询商品分类")
    @GetMapping("/list/{parentId}")
    public CommonResult<CommonPage<PmsProductCategory>> getList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = pmsProductCategoryService.getList(parentId, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(productCategoryList));
    }

    @ApiOperation("查询所有一级分类及子分类")
    @GetMapping("/list/withChildren")
    public CommonResult<List<PmsProductCategoryWithChildrenVo>> listWithChildren() {
        List<PmsProductCategoryWithChildrenVo> list = pmsProductCategoryService.listWithChildren();
        return CommonResult.success(list);
    }
}
