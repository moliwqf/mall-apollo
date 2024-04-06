package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.SmsCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-07 00:31:17
 * @description 优惠券dao层
 */
@Repository
public interface SmsCouponDao {
    /**
     * 根据商品id和商品分类id获取可用的优惠券信息
     */
    List<SmsCoupon> getAvailableCouponList(@Param("productId") Long productId, @Param("productCategoryId") Long productCategoryId);
}
