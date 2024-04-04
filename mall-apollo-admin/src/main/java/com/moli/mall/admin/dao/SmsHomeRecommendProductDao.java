package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.SmsHomeRecommendProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:17:50
 * @description 首页人气推荐管理dao
 */
@Repository
public interface SmsHomeRecommendProductDao {
    /**
     * 批量添加
     */
    int insertList(@Param("homeBrandList") List<SmsHomeRecommendProduct> homeBrandList);
}
