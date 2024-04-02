package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsProductAttributeValue;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 21:49:32
 * @description 商品参数及自定义规格属性dao层
 */
@Repository
public interface PmsProductAttributeValueDao {
    /**
     * 批量添加
     */
    int insertList(@Param("productAttributeValueList") List<PmsProductAttributeValue> productAttributeValueList);
}
