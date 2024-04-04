package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.dto.SmsCouponParams;
import com.moli.mall.admin.service.SmsCouponService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsCoupon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:58:57
 * @description 优惠券管理
 */
@RestController
@Api(tags = "SmsCouponController", value = "优惠券管理")
@RequestMapping("/coupon")
public class SmsCouponController {

    @Resource
    private SmsCouponService smsCouponService;

    @ApiOperation("添加优惠券")
    @PostMapping("/create")
    public CommonResult<?> add(@RequestBody SmsCouponParams couponParam) {
        int count = smsCouponService.create(couponParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("删除优惠券")
    @PostMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = smsCouponService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改优惠券")
    @PostMapping("/update/{id}")
    public CommonResult<?> update(@PathVariable Long id,
                                  @RequestBody SmsCouponParams couponParam) {
        int count = smsCouponService.update(id, couponParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("根据优惠券名称和类型分页获取优惠券列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<SmsCoupon>> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) Integer type) {
        List<SmsCoupon> couponList = smsCouponService.list(name, type, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(couponList));
    }

    @ApiOperation("获取单个优惠券的详细信息")
    @GetMapping("/{id}")
    public CommonResult<SmsCouponParams> getItem(@PathVariable Long id) {
        SmsCouponParams couponParam = smsCouponService.getItem(id);
        return CommonResult.success(couponParam);
    }
}
