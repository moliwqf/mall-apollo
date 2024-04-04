package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.SmsHomeRecommendProduct;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:10:10
 * @description 首页人气推荐管理
 */
public interface SmsHomeRecommendProductService {
    /**
     * 分页查询
     */
    List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 批量更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 更新排序
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量添加
     */
    int create(List<SmsHomeRecommendProduct> homeBrandList);
}
