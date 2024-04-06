package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.UmsMemberReceiveAddress;
import com.moli.mall.portal.service.UmsMemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 16:26:19
 * @description 会员收货地址管理
 */
@RestController
@Api(tags = "UmsMemberReceiveAddressController", value = "会员收货地址管理")
@RequestMapping("/member/address")
public class UmsMemberReceiveAddressController {

    @Resource
    private UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    @ApiOperation("添加收货地址")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<?> add(@RequestBody UmsMemberReceiveAddress address) {
        int count = umsMemberReceiveAddressService.add(address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = umsMemberReceiveAddressService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改收货地址")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<?> update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddress address) {
        int count = umsMemberReceiveAddressService.update(id, address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<UmsMemberReceiveAddress>> list() {
        List<UmsMemberReceiveAddress> addressList = umsMemberReceiveAddressService.list();
        return CommonResult.success(addressList);
    }

    @ApiOperation("获取收货地址详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable Long id) {
        UmsMemberReceiveAddress address = umsMemberReceiveAddressService.getItem(id);
        return CommonResult.success(address);
    }
}
