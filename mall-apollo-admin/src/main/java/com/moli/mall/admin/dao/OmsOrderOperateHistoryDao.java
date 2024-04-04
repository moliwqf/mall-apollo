package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.OmsOrderOperateHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 15:19:23
 * @description 订单操作历史记录dao层
 */
@Repository
public interface OmsOrderOperateHistoryDao {
    /**
     * 批量添加
     */
    int insertList(@Param("historyList") List<OmsOrderOperateHistory> historyList);
}
