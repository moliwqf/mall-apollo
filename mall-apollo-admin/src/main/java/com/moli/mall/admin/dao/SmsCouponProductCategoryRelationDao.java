package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.SmsCouponProductCategoryRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:17:54
 * @description 优惠券与商品分类绑定dao
 */
@Repository
public interface SmsCouponProductCategoryRelationDao {
    /**
     * 批量添加
     */
    int insertList(@Param("productCategoryRelationList") List<SmsCouponProductCategoryRelation> productCategoryRelationList);
}
