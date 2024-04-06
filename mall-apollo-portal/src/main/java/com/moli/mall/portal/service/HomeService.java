package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.CmsSubject;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductCategory;
import com.moli.mall.portal.vo.HomeContentVo;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 20:14:24
 * @description 首页内容管理
 */
public interface HomeService {
    /**
     * 首页内容页信息展示
     */
    HomeContentVo content();

    /**
     * 分页获取推荐商品
     */
    List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum);

    /**
     * 获取首页商品分类
     */
    List<PmsProductCategory> getProductCateList(Long parentId);

    /**
     * 根据分类获取专题
     * @param cateId 分类id
     */
    List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum);

    /**
     * 分页获取人气推荐商品
     */
    List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize);

    /**
     * 分页获取新品推荐商品
     */
    List<PmsProduct> newProductList(Integer pageNum, Integer pageSize);
}
