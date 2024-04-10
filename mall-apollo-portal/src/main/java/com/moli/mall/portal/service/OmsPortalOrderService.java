package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.portal.dto.OrderParams;
import com.moli.mall.portal.vo.ConfirmOrderVo;
import com.moli.mall.portal.vo.OmsOrderDetailVo;

import java.util.List;
import java.util.Map;

/**
 * @author moli
 * @time 2024-04-06 16:44:51
 * @description 订单管理
 */
public interface OmsPortalOrderService {
    /**
     * 根据订单状态分页获取订单信息
     */
    CommonPage<OmsOrderDetailVo> list(Integer status, Integer pageNum, Integer pageSize);

    /**
     * 根据购物车信息生成确认单信息
     * @param cartIds 购物车项id
     * @return 订单信息
     */
    ConfirmOrderVo generateConfirmOrder(List<Long> cartIds);

    /**
     * 根据购物车信息生成订单
     */
    Map<String, Object> generateOrder(OrderParams orderParam);

    /**
     * 取消订单
     * @param orderId 订单id
     */
    void cancelOrder(Long orderId);

    /**
     * 根据订单id获取订单详情
     */
    OmsOrderDetailVo detail(Long orderId);

    /**
     * 支付成功杰阔
     * @param orderId 订单id
     * @param payType 支付类型
     */
    Integer paySuccess(Long orderId, Integer payType);

    /**
     * 删除订单
     */
    void deleteOrder(Long orderId);

    /**
     * 确认收货
     */
    void confirmReceiveOrder(Long orderId);

}
