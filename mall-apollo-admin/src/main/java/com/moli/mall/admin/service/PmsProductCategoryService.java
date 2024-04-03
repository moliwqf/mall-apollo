package com.moli.mall.admin.service;

import com.moli.mall.admin.dto.PmsProductCategoryParams;
import com.moli.mall.admin.vo.PmsProductCategoryWithChildrenVo;
import com.moli.mall.mbg.model.PmsProductCategory;

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

    /**
     * 分页查询产品分类
     * @param parentId 父id
     */
    List<PmsProductCategory> getList(Long parentId, Integer pageNum, Integer pageSize);

    /**
     * 根据id获取
     */
    PmsProductCategory info(Long id);

    /**
     * 添加新的产品分类
     */
    int create(PmsProductCategoryParams productCategoryParam);

    /**
     * 更新产品分类
     * @param id 产品分类id
     * @param productCategoryParam 更新的信息
     */
    int update(Long id, PmsProductCategoryParams productCategoryParam);

    /**
     * 删除商品分类
     */
    int delete(Long id);
}
