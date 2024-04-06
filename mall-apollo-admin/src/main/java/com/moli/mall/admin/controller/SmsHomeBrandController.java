package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsHomeBrandService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.SmsHomeBrand;
import com.moli.mall.mbg.model.SmsHomeNewProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:41:21
 * @description 首页新品管理
 */
@RestController
@Api(tags = "SmsHomeBrandController", value = "首页品牌管理")
@RequestMapping("/home/brand")
public class SmsHomeBrandController {

    @Resource
    private SmsHomeBrandService smsHomeBrandService;

    @ApiOperation("添加首页推荐品牌")
    @PostMapping(value = "/create")
    public CommonResult<?> create(@RequestBody List<SmsHomeBrand> homeBrandList) {
        int count = smsHomeBrandService.create(homeBrandList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改品牌排序")
    @PostMapping(value = "/update/sort/{id}")
    public CommonResult<?> updateSort(@PathVariable Long id, Integer sort) {
        int count = smsHomeBrandService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量删除推荐品牌")
    @PostMapping(value = "/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = smsHomeBrandService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping(value = "/update/recommendStatus")
    public CommonResult<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = smsHomeBrandService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("分页查询推荐品牌")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsHomeBrand>> list(@RequestParam(value = "brandName", required = false) String brandName,
                                                       @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus) {
        List<SmsHomeBrand> homeBrandList = smsHomeBrandService.list(brandName, recommendStatus, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(homeBrandList));
    }

    @ApiOperation("分页查询主页推荐品牌app端")
    @GetMapping(value = "/app/list")
    public CommonResult<CommonPage<PmsBrand>> appList(@RequestParam(value = "brandName", required = false) String brandName,
                                                      @RequestParam(value = "showStatus", defaultValue = "1") Integer showStatus) {
        List<PmsBrand> homeBrandList = smsHomeBrandService.appList(brandName, showStatus, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(homeBrandList));
    }
}
