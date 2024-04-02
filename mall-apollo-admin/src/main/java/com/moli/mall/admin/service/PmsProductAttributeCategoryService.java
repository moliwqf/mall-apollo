package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.PmsProductAttributeCategory;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:55:57
 * @description 产品属性分类服务层
 */
public interface PmsProductAttributeCategoryService {
    /**
     * 分页获取产品属性分类信息
     */
    List<PmsProductAttributeCategory> getList(Integer pageNum, Integer pageSize);
}
