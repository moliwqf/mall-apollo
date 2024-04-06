package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.SmsHomeNewProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:33:15
 * @description 首页新品管理dao
 */
@Repository
public interface SmsHomeNewProductDao {
    /**
     * 批量添加
     */
    int insertList(@Param("homeBrandList") List<SmsHomeNewProduct> homeBrandList);

    /**
     * 分页查询人气推荐商品
     */
    List<PmsProduct> hotList(@Param("productName") String productName, @Param("page") int page, @Param("pageSize") Integer pageSize);

    /**
     * 分页查询新品推荐
     */
    List<PmsProduct> appList(@Param("productName") String productName, @Param("page") int page, @Param("pageSize") Integer pageSize);
}
