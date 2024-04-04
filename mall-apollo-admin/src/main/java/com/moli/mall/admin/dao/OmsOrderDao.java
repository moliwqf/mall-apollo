package com.moli.mall.admin.dao;

import com.moli.mall.admin.dto.OmsOrderDeliveryParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 15:03:33
 * @description 订单dao层
 */
@Repository
public interface OmsOrderDao {
    /**
     * 批量更新订单信息
     * @param orderList 订单信息
     * @return 更新行数
     */
    int updateDeliveryList(@Param("orderList") List<OmsOrderDeliveryParams> orderList);
}
