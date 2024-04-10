package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsOrder;
import com.moli.mall.portal.dto.OrderParams;
import com.moli.mall.portal.service.OmsPortalOrderService;
import com.moli.mall.portal.vo.ConfirmOrderVo;
import com.moli.mall.portal.vo.OmsOrderDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author moli
 * @time 2024-04-06 16:44:29
 * @description 订单管理
 */
@RestController
@Api(tags = "OmsPortalOrderController", value = "订单管理")
@RequestMapping("/order")
public class OmsPortalOrderController {

    @Resource
    private OmsPortalOrderService omsPortalOrderService;

    @ApiOperation("用户取消订单")
    @RequestMapping(value = "/cancelUserOrder", method = RequestMethod.POST)
    public CommonResult<?> cancelUserOrder(Long orderId) {
        omsPortalOrderService.cancelOrder(orderId);
        return CommonResult.success(null);
    }

    @ApiOperation("用户确认收货")
    @RequestMapping(value = "/confirmReceiveOrder", method = RequestMethod.POST)
    public CommonResult<?> confirmReceiveOrder(Long orderId) {
        omsPortalOrderService.confirmReceiveOrder(orderId);
        return CommonResult.success(null);
    }

    @ApiOperation("用户删除订单")
    @RequestMapping(value = "/deleteOrder", method = RequestMethod.POST)
    public CommonResult<?> deleteOrder(Long orderId) {
        omsPortalOrderService.deleteOrder(orderId);
        return CommonResult.success(null);
    }

    @ApiOperation("用户支付成功的回调")
    @RequestMapping(value = "/paySuccess", method = RequestMethod.POST)
    public CommonResult<?> paySuccess(@RequestParam Long orderId,
                                      @RequestParam Integer payType) {
        Integer count = omsPortalOrderService.paySuccess(orderId, payType);
        return CommonResult.success(count, "支付成功");
    }

    @ApiOperation("根据ID获取订单详情")
    @RequestMapping(value = "/detail/{orderId}", method = RequestMethod.GET)
    public CommonResult<OmsOrderDetailVo> detail(@PathVariable Long orderId) {
        OmsOrderDetailVo orderDetail = omsPortalOrderService.detail(orderId);
        return CommonResult.success(orderDetail);
    }

    @ApiOperation("根据购物车信息生成订单")
    @RequestMapping(value = "/generateOrder", method = RequestMethod.POST)
    public CommonResult<?> generateOrder(@RequestBody OrderParams orderParam) {
        Map<String, Object> result = omsPortalOrderService.generateOrder(orderParam);
        return CommonResult.success(result, "下单成功");
    }

    @ApiOperation("根据购物车信息生成确认单信息")
    @RequestMapping(value = "/generateConfirmOrder", method = RequestMethod.POST)
    public CommonResult<ConfirmOrderVo> generateConfirmOrder(@RequestBody List<Long> cartIds) {
        ConfirmOrderVo confirmOrderResult = omsPortalOrderService.generateConfirmOrder(cartIds);
        return CommonResult.success(confirmOrderResult);
    }

    @ApiOperation("按状态分页获取用户订单列表")
    @ApiImplicitParam(name = "status", value = "订单状态：-1->全部；0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
            defaultValue = "-1", allowableValues = "-1,0,1,2,3,4", paramType = "query", dataType = "int")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<OmsOrderDetailVo>> list(@RequestParam Integer status,
                                                           @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                           @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        CommonPage<OmsOrderDetailVo> orderPage = omsPortalOrderService.list(status, pageNum, pageSize);
        return CommonResult.success(orderPage);
    }
}
