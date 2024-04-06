package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.SmsHomeRecommendSubjectDao;
import com.moli.mall.admin.service.SmsHomeRecommendSubjectService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.SmsHomeRecommendSubjectMapper;
import com.moli.mall.mbg.model.CmsSubject;
import com.moli.mall.mbg.model.SmsHomeRecommendSubject;
import com.moli.mall.mbg.model.SmsHomeRecommendSubjectExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 22:13:32
 * @description 首页专题推荐管理
 */
@Service
public class SmsHomeRecommendSubjectServiceImpl implements SmsHomeRecommendSubjectService {

    @Resource
    private SmsHomeRecommendSubjectMapper smsHomeRecommendSubjectMapper;

    @Resource
    private SmsHomeRecommendSubjectDao smsHomeRecommendSubjectDao;

    @Override
    public List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsHomeRecommendSubjectExample smsHomeRecommendSubjectExample = new SmsHomeRecommendSubjectExample();
        SmsHomeRecommendSubjectExample.Criteria criteria = smsHomeRecommendSubjectExample.createCriteria();
        smsHomeRecommendSubjectExample.setOrderByClause("sort desc");

        if (StrUtil.isNotEmpty(subjectName)) {
            criteria.andSubjectNameLike("%" + subjectName + "%");
        }

        if (Objects.nonNull(recommendStatus)) {
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }

        return smsHomeRecommendSubjectMapper.selectByExample(smsHomeRecommendSubjectExample);
    }

    @Override
    @Transactional
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        if (CollectionUtils.isEmpty(ids)) return 0;

        SmsHomeRecommendSubject update = new SmsHomeRecommendSubject();
        update.setRecommendStatus(recommendStatus);
        SmsHomeRecommendSubjectExample smsHomeRecommendSubjectExample = new SmsHomeRecommendSubjectExample();
        smsHomeRecommendSubjectExample.createCriteria().andIdIn(ids);

        return smsHomeRecommendSubjectMapper.updateByExampleSelective(update, smsHomeRecommendSubjectExample);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;
        SmsHomeRecommendSubjectExample smsHomeRecommendSubjectExample = new SmsHomeRecommendSubjectExample();
        smsHomeRecommendSubjectExample.createCriteria().andIdIn(ids);
        return smsHomeRecommendSubjectMapper.deleteByExample(smsHomeRecommendSubjectExample);
    }

    @Override
    @Transactional
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendSubject raw = smsHomeRecommendSubjectMapper.selectByPrimaryKey(id);
        if (Objects.isNull(raw)) {
            AssetsUtil.fail("不存在该推荐主题");
        }
        SmsHomeRecommendSubject update = new SmsHomeRecommendSubject();
        update.setSort(sort);
        update.setId(id);
        return smsHomeRecommendSubjectMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    @Transactional
    public int create(List<SmsHomeRecommendSubject> homeBrandList) {
        if (CollectionUtils.isEmpty(homeBrandList)) {
            return 0;
        }
        for (SmsHomeRecommendSubject rs : homeBrandList) {
            if (Objects.isNull(rs.getSort())) {
                rs.setSort(0);
            }
        }
        return smsHomeRecommendSubjectDao.insertList(homeBrandList);
    }

    @Override
    public List<CmsSubject> appList(String subjectName, Integer pageNum, Integer pageSize) {
        Integer page = (pageNum - 1) * pageSize;
        return smsHomeRecommendSubjectDao.appList(subjectName, page, pageSize);
    }
}
