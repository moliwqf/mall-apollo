package com.moli.mall.portal.dao;

import com.moli.mall.portal.domain.PromotionProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-08 10:18:54
 * @description 前台系统自定义商品Dao
 */
@Repository
public interface PortalProductDao {
    /**
     * 根据商品id获取促销信息
     */
    List<PromotionProduct> getPromotionProductList(@Param("productIdList") List<Long> productIdList);
}
