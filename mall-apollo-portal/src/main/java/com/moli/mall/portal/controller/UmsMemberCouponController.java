package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsCoupon;
import com.moli.mall.portal.service.UmsMemberCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 15:45:10
 * @description 用户优惠券管理
 */
@RestController
@Api(tags = "UmsMemberCouponController", value = "用户优惠券管理")
@RequestMapping("/member/coupon")
public class UmsMemberCouponController {

    @Resource
    private UmsMemberCouponService umsMemberCouponService;

    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<SmsCoupon>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCoupon> couponList = umsMemberCouponService.list(useStatus);
        return CommonResult.success(couponList);
    }
}
