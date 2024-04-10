package com.moli.mall.search.service;

import com.moli.mall.search.domain.EsProduct;
import com.moli.mall.search.domain.EsProductRelatedInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-09 15:15:51
 * @description 搜索商品管理
 */
public interface EsProductService {
    /**
     * 导入所有数据库中商品到ES
     */
    int importAll();

    /**
     * 根据商品id删除商品
     */
    void delete(Long id);

    /**
     * 批量删除
     */
    void delete(List<Long> ids);

    /**
     * 创建商品
     */
    EsProduct create(Long id);

    /**
     * 简单搜索
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 综合搜索
     */
    Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 根据商品id推荐商品
     */
    Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize);

    /**
     * 获取搜索的相关品牌、分类及筛选属性
     */
    EsProductRelatedInfo searchRelatedInfo(String keyword);
}
