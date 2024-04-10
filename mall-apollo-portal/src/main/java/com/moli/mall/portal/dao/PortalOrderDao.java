package com.moli.mall.portal.dao;

import com.moli.mall.portal.vo.OmsOrderDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author moli
 * @time 2024-04-09 11:19:33
 * @description 订单dao层
 */
@Repository
public interface PortalOrderDao {
    /**
     * 根据订单id获取详情信息
     */
    OmsOrderDetailVo getDetail(@Param("orderId") Long orderId);
}
