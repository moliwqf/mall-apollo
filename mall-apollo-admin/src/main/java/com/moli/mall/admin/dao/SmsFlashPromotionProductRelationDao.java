package com.moli.mall.admin.dao;

import com.moli.mall.admin.vo.FlashPromotionProduct;
import com.moli.mall.mbg.model.SmsFlashPromotionProductRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 14:34:31
 * @description 秒杀场次和商品联系服务
 */
@Repository
public interface SmsFlashPromotionProductRelationDao {
    /**
     * 批量添加
     */
    int insertList(@Param("relationList") List<SmsFlashPromotionProductRelation> relationList);

    /**
     * 根据场次id和促销活动id获取商品种数
     * @param flashPromotionSessionId 场次id
     * @param flashPromotionId 促销活动id
     * @return long
     */
    Long getCount(@Param("flashPromotionSessionId") Long flashPromotionSessionId, @Param("flashPromotionId") Long flashPromotionId);
}
