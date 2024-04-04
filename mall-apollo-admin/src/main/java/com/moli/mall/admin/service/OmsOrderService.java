package com.moli.mall.admin.service;

import com.moli.mall.admin.dto.OmsOrderDeliveryParams;
import com.moli.mall.admin.dto.OmsOrderQueryParams;
import com.moli.mall.admin.vo.OmsOrderDetailVo;
import com.moli.mall.mbg.model.OmsOrder;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 07:55:23
 * @description 订单服务层
 */
public interface OmsOrderService {
    /**
     * 分页查询订单信息
     */
    List<OmsOrder> list(Integer pageNum, Integer pageSize, OmsOrderQueryParams queryParam);

    /**
     * 根据订单id获取定单详情信息
     */
    OmsOrderDetailVo detail(Long orderId);

    /**
     * 订单发货
     * @param deliveryParamList 发货信息
     */
    int delivery(List<OmsOrderDeliveryParams> deliveryParamList);

    /**
     * 更新订单备注信息
     * @param id 订单id
     * @param note 备注信息
     * @param status 状态
     */
    int updateNote(Long id, String note, Integer status);

    /**
     * 批量删除订单
     */
    int delete(List<Long> ids);

    /**
     * 批量关闭订单
     * @param note 备注信息
     */
    int close(List<Long> ids, String note);
}
