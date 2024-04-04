package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.OmsOrderSettingService;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsOrderSetting;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-04 16:41:09
 * @description 订单设置管理
 */
@RestController
@Api(tags = "OmsOrderSettingController", value = "订单设置管理")
@RequestMapping("/orderSetting")
public class OmsOrderSettingController {

    @Resource
    private OmsOrderSettingService omsOrderSettingService;

    @ApiOperation("修改指定订单设置")
    @PostMapping(value = "/update/{id}")
    public CommonResult<?> update(@PathVariable Long id,
                                  @RequestBody OmsOrderSetting orderSetting) {
        int count = omsOrderSettingService.update(id,orderSetting);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取指定订单设置")
    @GetMapping("/{id}")
    public CommonResult<OmsOrderSetting> info(@PathVariable Long id) {
        OmsOrderSetting orderSetting = omsOrderSettingService.info(id);
        return CommonResult.success(orderSetting);
    }
}
