package com.moli.mall.admin.service;

import com.moli.mall.admin.vo.PmsProductAttributeCategoryVo;
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

    /**
     * 获取产品属性分类及其属性
     */
    List<PmsProductAttributeCategoryVo> getListWithAttr();

    /**
     * 创建产品类型
     * @param name 名称
     * @return 是否成功
     */
    int create(String name);

    /**
     * 根据id更新产品类型
     */
    int update(Long id, String name);

    /**
     * 删除产品类型
     */
    int delete(Long id);
}
