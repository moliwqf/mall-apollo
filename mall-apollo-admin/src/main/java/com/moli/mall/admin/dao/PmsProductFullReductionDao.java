package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsProductFullReduction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 21:34:38
 * @description 商品满减价格设置dao层
 */
@Repository
public interface PmsProductFullReductionDao {
    /**
     * 批量添加新的商品满减价格
     * @param productFullReductionList 添加对象
     */
    int insertList(@Param("productFullReductionList") List<PmsProductFullReduction> productFullReductionList);
}
