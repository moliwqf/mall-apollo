package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductCategory;
import com.moli.mall.portal.service.PmsProductCategoryService;
import com.moli.mall.portal.service.PmsProductService;
import com.moli.mall.portal.vo.PmsPortalProductDetailVo;
import com.moli.mall.portal.vo.PmsProductCategoryWithChildrenVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 23:12:30
 * @description 前台产品模块
 */
@Api(tags = "PmsPortalProductController", value = "前台产品模块")
@RestController
@RequestMapping("/product")
public class PmsPortalProductController {

    @Resource
    private PmsProductService pmsProductService;

    @Resource
    private PmsProductCategoryService pmsProductCategoryService;

    @ApiOperation("获取前台商品详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonResult<PmsPortalProductDetailVo> detail(@PathVariable Long id) {
        CommonResult<PmsPortalProductDetailVo> detail = pmsProductService.detail(id);
        return CommonResult.success(detail.getData());
    }

    @ApiOperation("以树形结构获取所有商品分类")
    @RequestMapping(value = "/categoryTreeList", method = RequestMethod.GET)
    public CommonResult<List<PmsProductCategoryWithChildrenVo>> categoryTreeList() {
        return pmsProductCategoryService.categoryTreeList();
    }

    @ApiOperation(value = "综合搜索、筛选、排序")
    @ApiImplicitParam(name = "sort", value = "排序字段:0->按相关度；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低",
            defaultValue = "0", allowableValues = "0,1,2,3,4", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public CommonResult<CommonPage<PmsProduct>> search(@RequestParam(required = false) String keyword,
                                                       @RequestParam(required = false) Long brandId,
                                                       @RequestParam(required = false) Long productCategoryId,
                                                       @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                                                       @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                       @RequestParam(required = false, defaultValue = "0") Integer sort) {
        CommonPage<PmsProduct> productList = pmsProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return CommonResult.success(productList);
    }
}
