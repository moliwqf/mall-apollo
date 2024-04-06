package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.SmsHomeBrandDao;
import com.moli.mall.admin.service.SmsHomeBrandService;
import com.moli.mall.mbg.mapper.SmsHomeBrandMapper;
import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.SmsHomeBrand;
import com.moli.mall.mbg.model.SmsHomeBrandExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 23:41:57
 * @description 首页品牌管理
 */
@Service
public class SmsHomeBrandServiceImpl implements SmsHomeBrandService {

    @Resource
    private SmsHomeBrandMapper smsHomeBrandMapper;

    @Resource
    private SmsHomeBrandDao smsHomeBrandDao;

    @Override
    public List<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsHomeBrandExample smsHomeBrandExample = new SmsHomeBrandExample();
        smsHomeBrandExample.setOrderByClause("sort desc");
        SmsHomeBrandExample.Criteria criteria = smsHomeBrandExample.createCriteria();

        if (StrUtil.isNotEmpty(brandName)) {
            criteria.andBrandNameLike("%" + brandName + "%");
        }

        if (Objects.nonNull(recommendStatus)) {
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        return smsHomeBrandMapper.selectByExample(smsHomeBrandExample);
    }

    @Override
    @Transactional
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        if (CollectionUtils.isEmpty(ids)) return 0;
        SmsHomeBrand update = new SmsHomeBrand();
        update.setRecommendStatus(recommendStatus);

        SmsHomeBrandExample smsHomeBrandExample = new SmsHomeBrandExample();
        smsHomeBrandExample.createCriteria().andIdIn(ids);
        return smsHomeBrandMapper.updateByExampleSelective(update, smsHomeBrandExample);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;
        SmsHomeBrandExample smsHomeBrandExample = new SmsHomeBrandExample();
        smsHomeBrandExample.createCriteria().andIdIn(ids);
        return smsHomeBrandMapper.deleteByExample(smsHomeBrandExample);
    }

    @Override
    @Transactional
    public int updateSort(Long id, Integer sort) {
        SmsHomeBrand update = new SmsHomeBrand();
        update.setSort(sort);
        update.setId(id);

        return smsHomeBrandMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    @Transactional
    public int create(List<SmsHomeBrand> homeBrandList) {
        if (CollectionUtils.isEmpty(homeBrandList)) return 0;
        for (SmsHomeBrand brand : homeBrandList) {
            if (Objects.isNull(brand.getSort())) {
                brand.setSort(0);
            }
        }
        return smsHomeBrandDao.insertList(homeBrandList);
    }

    @Override
    public List<PmsBrand> appList(String brandName, Integer showStatus, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return smsHomeBrandDao.appList(brandName, showStatus);
    }
}
