package com.moli.mall.admin.service;

import com.moli.mall.admin.dto.SmsCouponParams;
import com.moli.mall.mbg.model.SmsCoupon;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:59:23
 * @description 优惠券管理
 */
public interface SmsCouponService {
    /**
     * 根据id获取信息
     */
    SmsCouponParams getItem(Long id);

    /**
     * 分页模糊查询
     */
    List<SmsCoupon> list(String name, Integer type, Integer pageNum, Integer pageSize);

    /**
     * 更新优惠券
     */
    int update(Long id, SmsCouponParams couponParam);

    /**
     * 删除
     */
    int delete(Long id);

    /**
     * 新建
     */
    int create(SmsCouponParams couponParam);
}
