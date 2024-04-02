package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.CmsSubjectProductRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 21:52:49
 * @description 专题和商品关系dao层
 */
@Repository
public interface CmsSubjectProductRelationDao {
    /**
     * 批量添加操作
     */
    int insertList(@Param("subjectProductRelationList") List<CmsSubjectProductRelation> subjectProductRelationList);
}
