package com.moli.mall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.PmsProductAttributeCategoryService;
import com.moli.mall.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.moli.mall.mbg.model.PmsProductAttributeCategory;
import com.moli.mall.mbg.model.PmsProductAttributeCategoryExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:56:11
 * @description 产品属性分类服务实现层
 */
@Service
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    @Resource
    private PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return pmsProductAttributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }
}
