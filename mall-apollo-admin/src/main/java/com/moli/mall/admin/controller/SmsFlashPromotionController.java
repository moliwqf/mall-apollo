package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsFlashPromotionService;
import com.moli.mall.admin.vo.HomeFlashPromotionVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsFlashPromotion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:31:09
 * @description 限时购活动管理
 */
@RestController
@Api(tags = "SmsFlashPromotionController", value = "限时购活动管理")
@RequestMapping("/flash")
public class SmsFlashPromotionController {

    @Resource
    private SmsFlashPromotionService smsFlashPromotionService;

    @ApiOperation("添加活动")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<?> create(@RequestBody SmsFlashPromotion flashPromotion) {
        int count = smsFlashPromotionService.create(flashPromotion);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("编辑活动信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<?> update(@PathVariable Long id, @RequestBody SmsFlashPromotion flashPromotion) {
        int count = smsFlashPromotionService.update(id, flashPromotion);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("删除活动信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = smsFlashPromotionService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改上下线状态")
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    public CommonResult<?> update(@PathVariable Long id, Integer status) {
        int count = smsFlashPromotionService.updateStatus(id, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取活动详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<?> getItem(@PathVariable Long id) {
        SmsFlashPromotion flashPromotion = smsFlashPromotionService.getItem(id);
        return CommonResult.success(flashPromotion);
    }

    @ApiOperation("根据活动名称分页查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<?> list(@RequestParam(value = "keyword", required = false) String keyword) {
        List<SmsFlashPromotion> flashPromotionList = smsFlashPromotionService.list(keyword, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(flashPromotionList));
    }

    @ApiOperation("获取当前秒杀场次信息")
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public CommonResult<HomeFlashPromotionVo> getCurrentFlashPromotion() {
        HomeFlashPromotionVo current = smsFlashPromotionService.getCurrentFlashPromotion();
        return CommonResult.success(current);
    }
}
