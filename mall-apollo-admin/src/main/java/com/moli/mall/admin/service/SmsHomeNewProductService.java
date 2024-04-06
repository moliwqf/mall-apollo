package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.SmsHomeNewProduct;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:26:01
 * @description 首页新品管理
 */
public interface SmsHomeNewProductService {
    /**
     * 分页查询
     */
    List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 更新排序字段
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量创建
     */
    int create(List<SmsHomeNewProduct> homeBrandList);

    /**
     * 分页查询人气推荐 app
     */
    List<PmsProduct> hotList(String productName, Integer pageNum, Integer pageSize);

    /**
     * 分页查询新品推荐
     */
    List<PmsProduct> appList(String productName, Integer pageNum, Integer pageSize);
}
