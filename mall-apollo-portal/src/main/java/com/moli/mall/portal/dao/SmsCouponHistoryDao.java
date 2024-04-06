package com.moli.mall.portal.dao;

import com.moli.mall.mbg.model.SmsCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 16:01:22
 * @description 优惠券历史记录
 */
@Repository
public interface SmsCouponHistoryDao {
    /**
     * 根据使用状态和用户id获取领取记录和使用记录
     * @param memberId memberId
     * @param useStatus 使用状态
     */
    List<SmsCoupon> listHistories(@Param("memberId") Long memberId, @Param("useStatus") Integer useStatus);

}
