package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.dto.OmsOrderDeliveryParams;
import com.moli.mall.admin.dto.OmsOrderQueryParams;
import com.moli.mall.admin.service.OmsOrderService;
import com.moli.mall.admin.vo.OmsOrderDetailVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 07:54:56
 * @description 订单管理模块
 */
@RestController
@Api(tags = "OmsOrderController", value = "订单管理模块")
@RequestMapping("/order")
public class OmsOrderController {

    @Resource
    private OmsOrderService omsOrderService;

    @ApiOperation("批量关闭订单")
    @PostMapping("/update/close")
    public CommonResult<?> close(@RequestParam("ids") List<Long> ids,
                                 @RequestParam String note) {
        int count = omsOrderService.close(ids, note);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量删除订单")
    @PostMapping("/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = omsOrderService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("备注订单")
    @PostMapping("/update/note")
    public CommonResult<?> updateNote(@RequestParam("id") Long id,
                                      @RequestParam("note") String note,
                                      @RequestParam("status") Integer status) {
        int count = omsOrderService.updateNote(id, note, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量发货")
    @PostMapping("/update/delivery")
    public CommonResult<?> delivery(@RequestBody List<OmsOrderDeliveryParams> deliveryParamList) {
        int count = omsOrderService.delivery(deliveryParamList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取订单详情:订单信息、商品信息、操作记录")
    @GetMapping("/{orderId}")
    public CommonResult<OmsOrderDetailVo> detail(@PathVariable Long orderId) {
        OmsOrderDetailVo orderDetailResult = omsOrderService.detail(orderId);
        return CommonResult.success(orderDetailResult);
    }

    @ApiOperation("查询订单")
    @GetMapping("/list")
    public CommonResult<CommonPage<OmsOrder>> list(OmsOrderQueryParams queryParam) {
        List<OmsOrder> orderList = omsOrderService.list(PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize(), queryParam);
        return CommonResult.success(CommonPage.restPage(orderList));
    }

    @ApiOperation("查询订单包括订单详情信息")
    @PostMapping("/detail/list")
    public CommonResult<CommonPage<OmsOrderDetailVo>> detailList(@RequestBody OmsOrderQueryParams queryParam) {
        List<OmsOrderDetailVo> orderList = omsOrderService.detailList(PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize(), queryParam);
        return CommonResult.success(CommonPage.restPage(orderList));
    }
}
