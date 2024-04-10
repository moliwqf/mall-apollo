package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.PmsProduct;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-07 10:06:43
 * @description 前台品牌管理
 */
public interface PmsPortalBrandService {
    /**
     * 根据品牌id分页获取商品信息
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);

    /**
     * 根据品牌id获取品牌信息
     */
    PmsBrand detail(Long brandId);

    /**
     * 获取推荐的品牌
     */
    List<PmsBrand> recommendList(Integer pageNum, Integer pageSize);
}
