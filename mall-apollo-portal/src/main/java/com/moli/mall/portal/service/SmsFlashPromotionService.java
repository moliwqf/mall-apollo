package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.portal.vo.FlashPromotionProduct;
import com.moli.mall.portal.vo.HomeFlashPromotionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 23:17:23
 * @description 限时购活动管理
 */
@FeignClient(value = "mall-admin", contextId = "SmsFlashPromotionService")
public interface SmsFlashPromotionService {

    @GetMapping("/flash/current")
    CommonResult<HomeFlashPromotionVo> getCurrentFlashPromotion();

    @GetMapping("/flashProductRelation/current/products")
    CommonResult<List<FlashPromotionProduct>> selectCurrentSessionProducts(@RequestParam(value = "flashPromotionId") Long flashPromotionId,
                                                                           @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId);
}
