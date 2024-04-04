package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.SmsFlashPromotionSessionService;
import com.moli.mall.admin.vo.SmsFlashPromotionSessionVo;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.SmsFlashPromotionSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
