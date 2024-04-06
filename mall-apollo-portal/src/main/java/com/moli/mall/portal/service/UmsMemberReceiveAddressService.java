package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 16:27:48
 * @description 会员收货地址管理
 */
public interface UmsMemberReceiveAddressService {
    /**
     * 添加收获地址
     */
    int add(UmsMemberReceiveAddress address);

    /**
     * 删除收获地址
     */
    int delete(Long id);

    /**
     * 更新地址
     */
    int update(Long id, UmsMemberReceiveAddress address);

    /**
     * 查询当前用户地址
     */
    List<UmsMemberReceiveAddress> list();

    /**
     * 获取地址详情
     */
    UmsMemberReceiveAddress getItem(Long id);
}
