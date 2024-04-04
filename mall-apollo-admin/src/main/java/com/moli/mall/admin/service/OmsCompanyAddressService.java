package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.OmsCompanyAddress;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 17:10:34
 * @description 收货地址管理
 */
public interface OmsCompanyAddressService {
    /**
     * 获取所有收货地址
     */
    List<OmsCompanyAddress> list();
}


