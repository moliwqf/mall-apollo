package com.moli.mall.portal.dao;

import com.moli.mall.mbg.model.PmsBrand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-07 10:14:11
 * @description 前台dao层
 */
@Repository
public interface HomeDao {
    /**
     * 分页获取推荐的品牌信息
     */
    List<PmsBrand> getRecommendBrandList(@Param("offset") int offset, @Param("pageSize") int pageSize);
}
