package com.moli.mall.search.repository;

import com.moli.mall.search.domain.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author moli
 * @time 2024-04-09 15:35:40
 * @description 搜索商品ES操作类
 */
public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long> {
    /**
     * 根据名称、副标题、关键字搜索
     */
    Page<EsProduct> findByNameOrSubTitleOrKeywords(String name, String subtitle, String keyword, Pageable pageRequest);
}
