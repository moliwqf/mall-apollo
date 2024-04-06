package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.SmsFlashPromotionSessionService;
import com.moli.mall.admin.vo.SmsFlashPromotionSessionVo;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsFlashPromotionSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:59:58
 * @description 限时购场次管理
 */
@RestController
@Api(tags = "SmsFlashPromotionSessionController", value = "限时购场次管理")
@RequestMapping("/flashSession")
public class SmsFlashPromotionSessionController {

    @Resource
    private SmsFlashPromotionSessionService smsFlashPromotionSessionService;

    @ApiOperation("添加场次")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<?> create(@RequestBody SmsFlashPromotionSession promotionSession) {
        int count = smsFlashPromotionSessionService.create(promotionSession);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("删除场次")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<?> delete(@PathVariable Long id) {
        int count = smsFlashPromotionSessionService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改场次")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public CommonResult<?> update(@PathVariable Long id,
                                  @RequestBody SmsFlashPromotionSession promotionSession) {
        int count = smsFlashPromotionSessionService.update(id, promotionSession);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改启用状态")
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    public CommonResult<?> updateStatus(@PathVariable Long id, Integer status) {
        int count = smsFlashPromotionSessionService.updateStatus(id, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取全部可选场次及其数量")
    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
    public CommonResult<List<SmsFlashPromotionSessionVo>> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionVo> promotionSessionList = smsFlashPromotionSessionService.selectList(flashPromotionId);
        return CommonResult.success(promotionSessionList);
    }

    @ApiOperation("获取全部场次")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<SmsFlashPromotionSession>> list() {
        List<SmsFlashPromotionSession> promotionSessionList = smsFlashPromotionSessionService.list();
        return CommonResult.success(promotionSessionList);
    }
}
