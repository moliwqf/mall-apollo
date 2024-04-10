package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.OmsCartItem;
import com.moli.mall.portal.vo.CartPromotionItemVo;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 23:04:17
 * @description 购物车管理
 */
public interface OmsCartItemService {
    /**
     * 获取用户的购物车信息
     */
    List<OmsCartItem> list();

    /**
     * 添加购物车商品
     * @param cartItem 购物车商品
     * @return 是否添加成功
     */
    int add(OmsCartItem cartItem);

    /**
     * 清空用户购物车信息
     */
    int clear();

    /**
     * 更新购物车项的数量
     */
    int updateQuantity(Long id, Integer quantity);

    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 批量删除
     */
    int delete(List<Long> ids, Long memberId);

    /**
     * 根据用户id和购物车id获取购物车项
     */
    List<CartPromotionItemVo> listPromotion(Long id, List<Long> cartIds);
}
