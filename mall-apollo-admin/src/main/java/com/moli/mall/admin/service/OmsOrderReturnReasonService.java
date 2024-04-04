package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.OmsOrderReturnReason;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 20:09:44
 * @description 退货原因管理
 */
public interface OmsOrderReturnReasonService {
    /**
     * 新增
     */
    int create(OmsOrderReturnReason returnReason);

    /**
     * 修改
     */
    int update(Long id, OmsOrderReturnReason returnReason);

    /**
     * 删除
     */
    int delete(List<Long> ids);

    /**
     * 分页查询
     */
    List<OmsOrderReturnReason> list(Integer pageNum, Integer pageSize);

    /**
     * 根据id获取
     */
    OmsOrderReturnReason getItem(Long id);

    /**
     * 批量更新状态是否可用
     */
    int updateStatus(List<Long> ids, Integer status);

}
