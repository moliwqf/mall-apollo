package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.portal.domain.MemberReadHistory;
import com.moli.mall.portal.service.MemberReadHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 17:22:52
 * @description 会员商品浏览记录管理
 */
@RestController
@Api(tags = "MemberReadHistoryController", value = "会员商品浏览记录管理")
@RequestMapping("/member/readHistory")
public class MemberReadHistoryController {

    @Resource
    private MemberReadHistoryService memberReadHistoryService;

    @ApiOperation("创建浏览记录")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<?> create(@RequestBody MemberReadHistory memberReadHistory) {
        int count = memberReadHistoryService.create(memberReadHistory);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("删除浏览记录")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult<?> delete(@RequestParam("ids") List<String> ids) {
        int count = memberReadHistoryService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("清空除浏览记录")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public CommonResult<?> clear() {
        memberReadHistoryService.clear();
        return CommonResult.success(null);
    }

    @ApiOperation("分页获取用户浏览记录")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MemberReadHistory>> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Page<MemberReadHistory> page = memberReadHistoryService.list(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(page));
    }
}
