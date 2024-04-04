package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.SmsCouponProductRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:16:44
 * @description 优惠券绑定商品dao
 */
@Repository
public interface SmsCouponProductRelationDao {
    /**
     * 批量添加
     */
    int insertList(@Param("productRelationList") List<SmsCouponProductRelation> productRelationList);
}
