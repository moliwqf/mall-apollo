package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.PmsProductCategoryService;
import com.moli.mall.admin.vo.PmsProductCategoryWithChildrenVo;
import com.moli.mall.common.domain.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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


    @ApiOperation("查询所有一级分类及子分类")
    @GetMapping("/list/withChildren")
    public CommonResult<List<PmsProductCategoryWithChildrenVo>> listWithChildren() {
        List<PmsProductCategoryWithChildrenVo> list = pmsProductCategoryService.listWithChildren();
        return CommonResult.success(list);
    }
}
