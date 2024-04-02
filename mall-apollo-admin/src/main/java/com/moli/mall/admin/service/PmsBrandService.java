package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.PmsBrand;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:33:41
 * @description 品牌服务层
 */
public interface PmsBrandService {

    /**
     * 分页模糊查询品牌信息
     */
    List<PmsBrand> listBrand(String keyword, Integer pageNum, Integer pageSize);
}
