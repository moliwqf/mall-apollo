package com.moli.mall.search.dao;

import com.moli.mall.search.domain.EsProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-09 15:22:23
 * @description 搜索商品管理自定义Dao
 */
@Repository
public interface EsProductDao {
    /**
     * 根据产品id获取es product
     */
    List<EsProduct> getAllEsProducts(@Param("productId") Long productId);
}
