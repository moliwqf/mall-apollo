package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.SmsHomeBrand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:52:54
 * @description 首页品牌管理
 */
@Repository
public interface SmsHomeBrandDao {
    /**
     * 批量添加
     */
    int insertList(@Param("homeBrandList") List<SmsHomeBrand> homeBrandList);

    /**
     * 分页查询
     */
    List<PmsBrand> appList(@Param("brandName") String brandName, @Param("showStatus") Integer showStatus);
}
