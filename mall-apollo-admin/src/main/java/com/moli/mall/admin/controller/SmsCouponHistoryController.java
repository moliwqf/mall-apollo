package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsCouponHistoryService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsCouponHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:37:06
 * @description 优惠券领取记录管理
 */
@RestController
@Api(tags = "SmsCouponHistoryController", value = "优惠券领取记录管理")
@RequestMapping("/couponHistory")
public class SmsCouponHistoryController {

    @Resource
    private SmsCouponHistoryService smsCouponHistoryService;

    @ApiOperation("根据优惠券id，使用状态，订单编号分页获取领取记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<SmsCouponHistory>> list(@RequestParam(value = "couponId", required = false) Long couponId,
                                                           @RequestParam(value = "useStatus", required = false) Integer useStatus,
                                                           @RequestParam(value = "orderSn", required = false) String orderSn) {
        List<SmsCouponHistory> historyList = smsCouponHistoryService.list(couponId, useStatus, orderSn, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(historyList));
    }
}
