package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsProductCategoryAttributeRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-03 16:16:45
 * @description 产品分裂与属性关联dao层
 */
@Repository
public interface PmsProductCategoryAttributeRelationDao {

    int insertList(@Param("addRelationList") List<PmsProductCategoryAttributeRelation> addRelationList);
}
