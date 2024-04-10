package com.moli.mall.portal.dao;

import com.moli.mall.mbg.model.OmsOrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-08 22:41:31
 * @description 订单项dao
 */
@Repository
public interface OmsOrderItemDao {
    /**
     * 批量插入
     */
    int insertList(@Param("orderItemList") List<OmsOrderItem> orderItemList);

    /**
     * 解除取消订单的库存锁定
     */
    int releaseSkuStockLock(@Param("itemList") List<OmsOrderItem> itemList);

    /**
     * 更新库存信息
     */
    int updateSkuStock(@Param("itemList") List<OmsOrderItem> itemList);

}
