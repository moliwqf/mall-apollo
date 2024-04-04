package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.SmsCouponHistory;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:42:27
 * @description 优惠券领取记录管理
 */
public interface SmsCouponHistoryService {
    /**
     * 分页获取领取记录
     */
    List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageNum, Integer pageSize);
}
