package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.PmsProductAttribute;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:53:56
 * @description 产品属性服务层
 */
public interface PmsProductAttributeService {
    /**
     * 根据分类查询属性列表或参数列表
     * @param cid 分页id
     * @param type 类型
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return PmsProductAttribute
     */
    List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageNum, Integer pageSize);
}
