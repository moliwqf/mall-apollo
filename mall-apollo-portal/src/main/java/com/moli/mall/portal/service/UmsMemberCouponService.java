package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.SmsCoupon;

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
}
