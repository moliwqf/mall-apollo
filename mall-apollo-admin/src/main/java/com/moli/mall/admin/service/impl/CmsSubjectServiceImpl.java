package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.CmsSubjectService;
import com.moli.mall.mbg.mapper.CmsSubjectMapper;
import com.moli.mall.mbg.model.CmsSubject;
import com.moli.mall.mbg.model.CmsSubjectExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CmsSubjectExample cmsSubjectExample = new CmsSubjectExample();
        if (StrUtil.isNotEmpty(keyword)) {
            cmsSubjectExample.createCriteria().andTitleLike("%" + keyword + "%");
        }
        return cmsSubjectMapper.selectByExample(cmsSubjectExample);
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CmsSubjectExample cmsSubjectExample = new CmsSubjectExample();
        if (Objects.nonNull(cateId)) {
            cmsSubjectExample.createCriteria().andCategoryIdEqualTo(cateId);
        }
        return cmsSubjectMapper.selectByExample(cmsSubjectExample);
    }
}
