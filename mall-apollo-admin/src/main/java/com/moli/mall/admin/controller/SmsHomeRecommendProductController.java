package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsHomeRecommendProductService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsHomeRecommendProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:09:42
 * @description 首页人气推荐管理
 */
@RestController
@Api(tags = "SmsHomeRecommendProductController", value = "首页人气推荐管理")
@RequestMapping("/home/recommendProduct")
public class SmsHomeRecommendProductController {

    @Resource
    private SmsHomeRecommendProductService smsHomeRecommendProductService;

    @ApiOperation("添加首页推荐")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody List<SmsHomeRecommendProduct> homeBrandList) {
        int count = smsHomeRecommendProductService.create(homeBrandList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public CommonResult<?> updateSort(@PathVariable Long id, Integer sort) {
        int count = smsHomeRecommendProductService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping("/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = smsHomeRecommendProductService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public CommonResult<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = smsHomeRecommendProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("分页查询推荐")
    @GetMapping("/list")
    public CommonResult<CommonPage<SmsHomeRecommendProduct>> list(@RequestParam(value = "productName", required = false) String productName,
                                                                  @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus) {
        List<SmsHomeRecommendProduct> homeBrandList = smsHomeRecommendProductService.list(productName, recommendStatus, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(homeBrandList));
    }
}
