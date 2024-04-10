package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.SmsCoupon;
import com.moli.mall.portal.vo.CartPromotionItemVo;
import com.moli.mall.portal.vo.SmsCouponHistoryDetailVo;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 15:45:42
 * @description 用户优惠券管理
 */
public interface UmsMemberCouponService {
    /**
     * 根据使用状态获取优惠券信息
     * @param useStatus 用户状态
     * @return 优惠券
     */
    List<SmsCoupon> list(Integer useStatus);

    /**
     * 获取用户可用的优惠券信息
     * @param cartPromotionItemList 购物车项集合
     * @param type 优惠券类型 1-表示可使用 0-表示不可用
     * @return 优惠券详情
     */
    List<SmsCouponHistoryDetailVo> listCoupons(List<CartPromotionItemVo> cartPromotionItemList, int type);
}
