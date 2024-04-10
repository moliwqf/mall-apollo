package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.OmsCartItem;
import com.moli.mall.portal.vo.CartPromotionItemVo;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-08 10:16:51
 * @description 促销管理Service
 */
public interface OmsPromotionService {
    /**
     * 根据购物车项获取优惠后的购物车项信息
     */
    List<CartPromotionItemVo> calcCartPromotion(List<OmsCartItem> cartItemList);
}
