package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.UmsResourceCategory;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-01 15:03:11
 * @description 资源分类服务层
 */
public interface UmsResourceCategoryService {
    /**
     * 查询所有的资源分类信息
     * @return UmsResourceCategory
     */
    List<UmsResourceCategory> listAll();
}
