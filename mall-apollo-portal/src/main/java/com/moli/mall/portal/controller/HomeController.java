package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.CmsSubject;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductCategory;
import com.moli.mall.portal.service.HomeService;
import com.moli.mall.portal.vo.HomeContentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 20:13:11
 * @description 首页内容管理
 */
@RestController
@Api(tags = "HomeController", value = "首页内容管理")
@RequestMapping("/home")
public class HomeController {

    @Resource
    private HomeService homeService;

    @ApiOperation("分页获取人气推荐商品")
    @RequestMapping(value = "/hotProductList", method = RequestMethod.GET)
    public CommonResult<List<PmsProduct>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.hotProductList(pageNum, pageSize);
        return CommonResult.success(productList);
    }

    @ApiOperation("分页获取新品推荐商品")
    @RequestMapping(value = "/newProductList", method = RequestMethod.GET)
    public CommonResult<List<PmsProduct>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.newProductList(pageNum, pageSize);
        return CommonResult.success(productList);
    }

    @ApiOperation("根据分类获取专题")
    @RequestMapping(value = "/subjectList", method = RequestMethod.GET)
    public CommonResult<List<CmsSubject>> getSubjectList(@RequestParam(required = false) Long cateId,
                                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubject> subjectList = homeService.getSubjectList(cateId, pageSize, pageNum);
        return CommonResult.success(subjectList);
    }

    @ApiOperation("获取首页商品分类")
    @RequestMapping(value = "/productCateList/{parentId}", method = RequestMethod.GET)
    public CommonResult<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
        return CommonResult.success(productCategoryList);
    }

    @ApiOperation("分页获取推荐商品")
    @RequestMapping(value = "/recommendProductList", method = RequestMethod.GET)
    public CommonResult<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
        return CommonResult.success(productList);
    }

    @ApiOperation("首页内容页信息展示")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public CommonResult<HomeContentVo> content() {
        HomeContentVo contentResult = homeService.content();
        return CommonResult.success(contentResult);
    }
}
