package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsCartItem;
import com.moli.mall.portal.service.OmsCartItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 23:03:44
 * @description 购物车管理
 */
@RestController
@Api(tags = "OmsCartItemController", value = "购物车管理")
@RequestMapping("/cart")
public class OmsCartItemController {

    @Resource
    private OmsCartItemService omsCartItemService;

    @ApiOperation("获取某个会员的购物车列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<OmsCartItem>> list() {
        List<OmsCartItem> cartItemList = omsCartItemService.list();
        return CommonResult.success(cartItemList);
    }
}
