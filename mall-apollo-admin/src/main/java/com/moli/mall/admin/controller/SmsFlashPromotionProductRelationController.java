package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsFlashPromotionProductRelationService;
import com.moli.mall.admin.vo.FlashPromotionProduct;
import com.moli.mall.admin.vo.SmsFlashPromotionProductVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsFlashPromotionProductRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 14:28:46
 * @description 限时购和商品关系管理
 */
@RestController
@Api(tags = "SmsFlashPromotionProductRelationController", value = "限时购和商品关系管理")
@RequestMapping("/flashProductRelation")
public class SmsFlashPromotionProductRelationController {

    @Resource
    private SmsFlashPromotionProductRelationService smsFlashPromotionProductRelationService;

    @ApiOperation("批量选择商品添加关联")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<?> create(@RequestBody List<SmsFlashPromotionProductRelation> relationList) {
        int count = smsFlashPromotionProductRelationService.create(relationList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改关联相关信息")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<?> update(@PathVariable Long id, @RequestBody SmsFlashPromotionProductRelation relation) {
        int count = smsFlashPromotionProductRelationService.update(id, relation);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("删除关联")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = smsFlashPromotionProductRelationService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取管理商品促销信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult<SmsFlashPromotionProductRelation> getItem(@PathVariable Long id) {
        SmsFlashPromotionProductRelation relation = smsFlashPromotionProductRelationService.getItem(id);
        return CommonResult.success(relation);
    }

    @ApiOperation("分页查询不同场次关联及商品信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<SmsFlashPromotionProductVo>> list(@RequestParam(value = "flashPromotionId") Long flashPromotionId,
                                                                     @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId) {
        List<SmsFlashPromotionProductVo> flashPromotionProductList = smsFlashPromotionProductRelationService.list(flashPromotionId, flashPromotionSessionId, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(flashPromotionProductList));
    }

    @ApiOperation("当前场次下获取所有商品")
    @RequestMapping(value = "/current/products", method = RequestMethod.GET)
    public CommonResult<List<FlashPromotionProduct>> selectCurrentSessionProducts(@RequestParam(value = "flashPromotionId") Long flashPromotionId,
                                                                                  @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId) {
        List<FlashPromotionProduct> productList = smsFlashPromotionProductRelationService.selectCurrentSessionProducts(flashPromotionId, flashPromotionSessionId);
        return CommonResult.success(productList);
    }
}
