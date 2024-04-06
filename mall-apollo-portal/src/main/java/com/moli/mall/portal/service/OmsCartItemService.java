package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.OmsCartItem;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 23:04:17
 * @description 购物车管理
 */
public interface OmsCartItemService {
    /**
     * 获取用户的购物车信息
     */
    List<OmsCartItem> list();
}
