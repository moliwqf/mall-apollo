package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.PmsProductAttributeService;
import com.moli.mall.admin.vo.ProductAttrInfoVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProductAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:53:28
 * @description 产品属性模块
 */
@Api(tags = "PmsProductAttributeController", value = "产品属性模块")
@RestController
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {

    @Resource
    private PmsProductAttributeService pmsProductAttributeService;

    @ApiOperation("根据商品分类的id获取商品属性及属性分类")
    @GetMapping("/attrInfo/{productCategoryId}")
    public CommonResult<List<ProductAttrInfoVo>> getAttrInfo(@PathVariable Long productCategoryId) {
        List<ProductAttrInfoVo> productAttrInfoList = pmsProductAttributeService.getProductAttrInfo(productCategoryId);
        return CommonResult.success(productAttrInfoList);
    }

    @ApiOperation("根据分类查询属性列表或参数列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "0表示属性，1表示参数", required = true, paramType = "query", dataType = "integer")})
    @GetMapping("/list/{cid}")
    public CommonResult<CommonPage<PmsProductAttribute>> getList(@PathVariable Long cid, @RequestParam(value = "type") Integer type) {
        List<PmsProductAttribute> productAttributeList = pmsProductAttributeService.getList(cid, type, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(productAttributeList));
    }
}
