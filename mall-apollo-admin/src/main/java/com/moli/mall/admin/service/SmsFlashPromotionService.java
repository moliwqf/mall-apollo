package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.SmsFlashPromotion;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:31:32
 * @description 限时购活动管理
 */
public interface SmsFlashPromotionService {
    /**
     * 分页查询
     */
    List<SmsFlashPromotion> list(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 获取秒杀信息
     */
    SmsFlashPromotion getItem(Long id);

    /**
     * 更新状态
     */
    int updateStatus(Long id, Integer status);

    /**
     * 删除
     */
    int delete(Long id);

    /**
     * 更新信息
     */
    int update(Long id, SmsFlashPromotion flashPromotion);

    /**
     * 创建新秒杀活动
     */
    int create(SmsFlashPromotion flashPromotion);
}
