package com.moli.mall.admin.service;

import com.moli.mall.admin.vo.FlashPromotionProduct;
import com.moli.mall.admin.vo.SmsFlashPromotionProductVo;
import com.moli.mall.mbg.model.SmsFlashPromotionProductRelation;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 14:29:09
 * @description 秒杀场次和商品联系服务
 */
public interface SmsFlashPromotionProductRelationService {
    /**
     * 批量添加
     */
    int create(List<SmsFlashPromotionProductRelation> relationList);

    /**
     * 更新
     */
    int update(Long id, SmsFlashPromotionProductRelation relation);

    /**
     * 删除
     */
    int delete(Long id);

    /**
     * 根据id获取
     */
    SmsFlashPromotionProductRelation getItem(Long id);

    /**
     * 分页查询
     */
    List<SmsFlashPromotionProductVo> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageNum, Integer pageSize);

    /**
     * 获取所有商品
     */
    List<FlashPromotionProduct> selectCurrentSessionProducts(Long promotionId, Long promotionSessionId);
}
