package com.moli.mall.admin.service;

import com.moli.mall.admin.vo.PmsProductCategoryWithChildrenVo;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:14:54
 * @description 产品分类服务层
 */
public interface PmsProductCategoryService {

    /**
     * 查询所有一级分类及子分类
     */
    List<PmsProductCategoryWithChildrenVo> listWithChildren();
}
