package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.SmsHomeBrand;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 23:41:45
 * @description 首页品牌管理
 */
public interface SmsHomeBrandService {
    /**
     * 分页查询
     */
    List<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 更新排序
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量添加
     */
    int create(List<SmsHomeBrand> homeBrandList);

    /**
     * 根据品牌名和显示状态分页查询
     */
    List<PmsBrand> appList(String brandName, Integer showStatus, Integer pageNum, Integer pageSize);

}
