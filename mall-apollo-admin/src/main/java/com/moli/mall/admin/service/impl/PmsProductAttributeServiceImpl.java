package com.moli.mall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.PmsProductAttributeService;
import com.moli.mall.mbg.mapper.PmsProductAttributeMapper;
import com.moli.mall.mbg.model.PmsProductAttribute;
import com.moli.mall.mbg.model.PmsProductAttributeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-02 15:54:10
 * @description 产品属性服务实现层
 */
@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    @Resource
    private PmsProductAttributeMapper pmsProductAttributeMapper;

    @Override
    public List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductAttributeExample pmsProductAttributeExample = new PmsProductAttributeExample();
        pmsProductAttributeExample.setOrderByClause("sort desc");
        pmsProductAttributeExample.createCriteria()
                .andProductAttributeCategoryIdEqualTo(cid)
                .andTypeEqualTo(type);
        return pmsProductAttributeMapper.selectByExample(pmsProductAttributeExample);
    }
}
