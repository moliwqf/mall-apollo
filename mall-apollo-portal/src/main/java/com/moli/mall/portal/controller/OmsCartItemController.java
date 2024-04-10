package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsCartItem;
import com.moli.mall.portal.service.OmsCartItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("删除购物车中的某个商品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = omsCartItemService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改购物车中某个商品的数量")
    @RequestMapping(value = "/update/quantity", method = RequestMethod.GET)
    public CommonResult<?> updateQuantity(@RequestParam Long id,
                                          @RequestParam Integer quantity) {
        int count = omsCartItemService.updateQuantity(id, quantity);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("清空购物车")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public CommonResult<?> clear() {
        int count = omsCartItemService.clear();
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("添加商品到购物车")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<?> add(@RequestBody OmsCartItem cartItem) {
        int count = omsCartItemService.add(cartItem);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取某个会员的购物车列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<OmsCartItem>> list() {
        List<OmsCartItem> cartItemList = omsCartItemService.list();
        return CommonResult.success(cartItemList);
    }
}
