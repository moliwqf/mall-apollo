package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsHomeAdvertiseService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsHomeAdvertise;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 20:29:21
 * @description 首页轮播广告管理
 */
@RestController
@Api(tags = "SmsHomeAdvertiseController", value = "首页轮播广告管理")
@RequestMapping("/home/advertise")
public class SmsHomeAdvertiseController {

    @Resource
    private SmsHomeAdvertiseService smsHomeAdvertiseService;

    @ApiOperation("添加广告")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody SmsHomeAdvertise advertise) {
        int count = smsHomeAdvertiseService.create(advertise);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.fail();
    }

    @ApiOperation("删除广告")
    @PostMapping("/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = smsHomeAdvertiseService.delete(ids);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.fail();
    }

    @ApiOperation("修改上下线状态")
    @PostMapping("/update/status/{id}")
    public CommonResult<?> updateStatus(@PathVariable Long id, Integer status) {
        int count = smsHomeAdvertiseService.updateStatus(id, status);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.fail();
    }

    @ApiOperation("修改广告")
    @PostMapping("/update/{id}")
    public CommonResult<?> update(@PathVariable Long id,
                                  @RequestBody SmsHomeAdvertise advertise) {
        int count = smsHomeAdvertiseService.update(id, advertise);
        if (count > 0)
            return CommonResult.success(count);
        return CommonResult.fail();
    }

    @ApiOperation("获取广告详情")
    @GetMapping("/{id}")
    public CommonResult<SmsHomeAdvertise> getItem(@PathVariable Long id) {
        SmsHomeAdvertise advertise = smsHomeAdvertiseService.getItem(id);
        return CommonResult.success(advertise);
    }

    @ApiOperation("分页查询广告")
    @GetMapping("/list")
    public CommonResult<CommonPage<SmsHomeAdvertise>> list(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) Integer type,
                                                           @RequestParam(value = "endTime", required = false) String endTime) {
        List<SmsHomeAdvertise> advertiseList = smsHomeAdvertiseService.list(name, type, endTime, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(advertiseList));
    }
}
