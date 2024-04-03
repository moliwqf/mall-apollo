package com.moli.mall.admin.dao;

import com.moli.mall.admin.vo.PmsProductAttributeCategoryVo;
import com.moli.mall.admin.vo.ProductAttrInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-03 10:54:13
 * @description 产品属性dao层
 */
@Repository
public interface PmsProductAttributeDao {
    /**
     * 获取所有的属性分类及其属性
     */
    List<PmsProductAttributeCategoryVo> getListWithAttr();

    /**
     * 根据产品分类id获取产品属性信息
     * @param productCategoryId 产品分类id
     * @return 产品属性及分类信息
     */
    List<ProductAttrInfoVo> getProductAttrInfo(@Param("productCategoryId") Long productCategoryId);
}
