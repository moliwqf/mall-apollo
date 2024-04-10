package com.moli.mall.search.domain;

import lombok.Data;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-09 16:11:28
 * @description 搜索商品的品牌名称，分类名称及属性
 */
@Data
public class EsProductRelatedInfo {

    private List<String> brandNames;

    private List<String> productCategoryNames;

    private List<ProductAttr> productAttrs;

    @Data
    public static class ProductAttr{

        private Long attrId;

        private String attrName;

        private List<String> attrValues;
    }
}
