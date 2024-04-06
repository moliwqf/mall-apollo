package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsHomeNewProductService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.SmsHomeNewProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:25:37
 * @description 首页新品管理
 */
@RestController
@Api(tags = "SmsHomeNewProductController", value = "首页新品管理")
@RequestMapping("/home/newProduct")
public class SmsHomeNewProductController {

    @Resource
    private SmsHomeNewProductService smsHomeNewProductService;

    @ApiOperation("添加首页推荐品牌")
    @PostMapping(value = "/create")
    public CommonResult<?> create(@RequestBody List<SmsHomeNewProduct> homeBrandList) {
        int count = smsHomeNewProductService.create(homeBrandList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping(value = "/update/sort/{id}")
    public CommonResult<?> updateSort(@PathVariable Long id, Integer sort) {
        int count = smsHomeNewProductService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping(value = "/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = smsHomeNewProductService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping(value = "/update/recommendStatus")
    public CommonResult<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                                 @RequestParam Integer recommendStatus) {
        int count = smsHomeNewProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("分页查询推荐")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsHomeNewProduct>> list(@RequestParam(value = "productName", required = false) String productName,
                                                            @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus) {
        List<SmsHomeNewProduct> homeNewProducts = smsHomeNewProductService.list(productName, recommendStatus, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(homeNewProducts));
    }

    @ApiOperation("分页查询新品推荐商品")
    @GetMapping(value = "/app/list")
    public CommonResult<List<PmsProduct>> appList(@RequestParam(value = "productName", required = false) String productName) {
        List<PmsProduct> homeProductList = smsHomeNewProductService.appList(productName, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(homeProductList);
    }

    @ApiOperation("分页查询人气推荐商品")
    @GetMapping(value = "/hotList")
    public CommonResult<List<PmsProduct>> hotList(@RequestParam(value = "productName", required = false) String productName) {
        List<PmsProduct> homeProductList = smsHomeNewProductService.hotList(productName, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(homeProductList);
    }
}
