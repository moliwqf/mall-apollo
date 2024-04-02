package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.CmsPrefrenceArea;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 16:00:53
 * @description 商品优选服务层
 */
public interface CmsPreferenceAreaService {
    /**
     * 获取所有商品优选
     */
    List<CmsPrefrenceArea> listAll();
}
