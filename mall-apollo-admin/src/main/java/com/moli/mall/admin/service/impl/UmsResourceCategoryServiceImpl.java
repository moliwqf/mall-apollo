package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.UmsResourceCategoryService;
import com.moli.mall.mbg.mapper.UmsResourceCategoryMapper;
import com.moli.mall.mbg.model.UmsResourceCategory;
import com.moli.mall.mbg.model.UmsResourceCategoryExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-01 15:03:29
 * @description 资源分类服务实现层
 */
@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {

    @Resource
    private UmsResourceCategoryMapper umsResourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> listAll() {
        UmsResourceCategoryExample umsResourceCategoryExample = new UmsResourceCategoryExample();
        umsResourceCategoryExample.setOrderByClause("sort desc");
        return umsResourceCategoryMapper.selectByExample(umsResourceCategoryExample);
    }
}
