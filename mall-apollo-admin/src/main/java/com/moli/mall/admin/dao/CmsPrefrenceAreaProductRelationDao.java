package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.CmsPrefrenceAreaProductRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 21:55:54
 * @description 优选专区和商品的关系dao层
 */
@Repository
public interface CmsPrefrenceAreaProductRelationDao {
    /**
     * 批量添加操作
     */
    int insertList(@Param("prefrenceAreaProductRelationList") List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList);
}
