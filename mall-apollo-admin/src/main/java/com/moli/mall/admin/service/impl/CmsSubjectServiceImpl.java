package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.CmsSubjectService;
import com.moli.mall.mbg.mapper.CmsSubjectMapper;
import com.moli.mall.mbg.model.CmsSubject;
import com.moli.mall.mbg.model.CmsSubjectExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 16:42:36
 * @description 商品专题服务实现层
 */
@Service
public class CmsSubjectServiceImpl implements CmsSubjectService {

    @Resource
    private CmsSubjectMapper cmsSubjectMapper;

    @Override
    public List<CmsSubject> listAll() {
        return cmsSubjectMapper.selectByExample(new CmsSubjectExample());
    }
}
