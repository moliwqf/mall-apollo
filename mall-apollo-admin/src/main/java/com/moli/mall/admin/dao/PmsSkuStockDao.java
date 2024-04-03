package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsSkuStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 21:43:39
 * @description 商品sku信息dao层
 */
@Repository
public interface PmsSkuStockDao {
    /**
     * 批量添加商品库存信息
     * @param skuStockList 库存信息
     */
    int insertList(@Param("skuStockList") List<PmsSkuStock> skuStockList);

    /**
     * 添加或更新sku
     */
    int replaceList(@Param("skuStockList") List<PmsSkuStock> skuStockList);
}
