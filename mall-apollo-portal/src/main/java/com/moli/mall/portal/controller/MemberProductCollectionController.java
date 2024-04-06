package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.portal.domain.MemberProductCollection;
import com.moli.mall.portal.service.MemberProductCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-06 22:50:31
 * @description 会员收藏管理
 */
@RestController
@Api(tags = "MemberCollectionController", value = "会员收藏管理")
@RequestMapping("/member/productCollection")
public class MemberProductCollectionController {

    @Resource
    private MemberProductCollectionService memberProductCollectionService;

    @ApiOperation("添加商品收藏")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<?> add(@RequestBody MemberProductCollection productCollection) {
        int count = memberProductCollectionService.add(productCollection);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("删除收藏商品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult<?> delete(Long productId) {
        int count = memberProductCollectionService.delete(productId);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("显示收藏列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MemberProductCollection>> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<MemberProductCollection> page = memberProductCollectionService.list(pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }

    @ApiOperation("显示收藏商品详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonResult<MemberProductCollection> detail(@RequestParam Long productId) {
        MemberProductCollection memberProductCollection = memberProductCollectionService.detail(productId);
        return CommonResult.success(memberProductCollection);
    }

    @ApiOperation("清空收藏列表")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public CommonResult<?> clear() {
        memberProductCollectionService.clear();
        return CommonResult.success(null);
    }
}
