package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.OmsOrderSetting;

/**
 * @author moli
 * @time 2024-04-04 16:41:35
 * @description 订单设置管理
 */
public interface OmsOrderSettingService {
    /**
     * 获取设置信息
     */
    OmsOrderSetting info(Long id);

    /**
     * 更新订单设置
     */
    int update(Long id, OmsOrderSetting orderSetting);
}
