package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.PmsSkuStock;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-03 10:27:30
 * @description 商品库存服务层
 */
public interface PmsSkuStockService {
    /**
     * 根据商品编号及编号模糊搜索sku库存
     * @param pid 商品编号
     * @param keyword key
     * @return sku信息
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新sku信息
     * @param pid 产品id
     * @param skuStockList sku信息
     * @return 影响的行数
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);
}
