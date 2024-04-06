package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.portal.vo.OmsOrderDetailVo;

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
}
