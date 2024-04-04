package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.SmsHomeRecommendProductDao;
import com.moli.mall.admin.service.SmsHomeRecommendProductService;
import com.moli.mall.mbg.mapper.SmsHomeRecommendProductMapper;
import com.moli.mall.mbg.model.SmsHomeRecommendProduct;
import com.moli.mall.mbg.model.SmsHomeRecommendProductExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 23:10:21
 * @description 首页人气推荐管理
 */
@Service
public class SmsHomeRecommendProductServiceImpl implements SmsHomeRecommendProductService {

    @Resource
    private SmsHomeRecommendProductMapper smsHomeRecommendProductMapper;

    @Resource
    private SmsHomeRecommendProductDao smsHomeRecommendProductDao;

    @Override
    public List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsHomeRecommendProductExample smsHomeRecommendProductExample = new SmsHomeRecommendProductExample();
        smsHomeRecommendProductExample.setOrderByClause("sort desc");
        SmsHomeRecommendProductExample.Criteria criteria = smsHomeRecommendProductExample.createCriteria();

        if (StrUtil.isNotEmpty(productName)) {
            criteria.andProductNameLike("%" + productName + "%");
        }

        if (Objects.nonNull(recommendStatus)) {
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        return smsHomeRecommendProductMapper.selectByExample(smsHomeRecommendProductExample);
    }

    @Override
    @Transactional
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        SmsHomeRecommendProduct update = new SmsHomeRecommendProduct();
        update.setRecommendStatus(recommendStatus);

        SmsHomeRecommendProductExample smsHomeRecommendProductExample = new SmsHomeRecommendProductExample();
        smsHomeRecommendProductExample.createCriteria().andIdIn(ids);
        return smsHomeRecommendProductMapper.updateByExampleSelective(update, smsHomeRecommendProductExample);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        SmsHomeRecommendProductExample smsHomeRecommendProductExample = new SmsHomeRecommendProductExample();
        smsHomeRecommendProductExample.createCriteria().andIdIn(ids);
        return smsHomeRecommendProductMapper.deleteByExample(smsHomeRecommendProductExample);
    }

    @Override
    @Transactional
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendProduct update = new SmsHomeRecommendProduct();
        update.setSort(sort);
        update.setId(id);

        return smsHomeRecommendProductMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    @Transactional
    public int create(List<SmsHomeRecommendProduct> homeBrandList) {
        if (CollectionUtils.isEmpty(homeBrandList)) {
            return 0;
        }
        for (SmsHomeRecommendProduct product : homeBrandList) {
            if (Objects.isNull(product.getSort())) {
                product.setSort(0);
            }
        }
        return smsHomeRecommendProductDao.insertList(homeBrandList);
    }
}
