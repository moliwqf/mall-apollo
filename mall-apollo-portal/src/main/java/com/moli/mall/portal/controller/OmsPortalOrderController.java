package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.portal.service.OmsPortalOrderService;
import com.moli.mall.portal.vo.OmsOrderDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
