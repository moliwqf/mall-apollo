package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.SmsHomeNewProductDao;
import com.moli.mall.admin.service.SmsHomeNewProductService;
import com.moli.mall.mbg.mapper.PmsProductMapper;
import com.moli.mall.mbg.mapper.SmsHomeNewProductMapper;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.SmsHomeNewProduct;
import com.moli.mall.mbg.model.SmsHomeNewProductExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 23:26:08
 * @description 首页新品管理
 */
@Service
public class SmsHomeNewProductServiceImpl implements SmsHomeNewProductService {

    @Resource
    private SmsHomeNewProductMapper smsHomeNewProductMapper;

    @Resource
    private SmsHomeNewProductDao smsHomeNewProductDao;

    @Override
    public List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsHomeNewProductExample smsHomeNewProductExample = new SmsHomeNewProductExample();
        smsHomeNewProductExample.setOrderByClause("sort desc");
        SmsHomeNewProductExample.Criteria criteria = smsHomeNewProductExample.createCriteria();

        if (StrUtil.isNotEmpty(productName)) {
            criteria.andProductNameLike("%" + productName + "%");
        }

        if (Objects.nonNull(recommendStatus)) {
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        return smsHomeNewProductMapper.selectByExample(smsHomeNewProductExample);
    }

    @Override
    @Transactional
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        SmsHomeNewProduct update = new SmsHomeNewProduct();
        update.setRecommendStatus(recommendStatus);
        SmsHomeNewProductExample smsHomeNewProductExample = new SmsHomeNewProductExample();
        smsHomeNewProductExample.createCriteria().andIdIn(ids);
        return smsHomeNewProductMapper.updateByExampleSelective(update, smsHomeNewProductExample);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        SmsHomeNewProductExample smsHomeNewProductExample = new SmsHomeNewProductExample();
        smsHomeNewProductExample.createCriteria().andIdIn(ids);
        return smsHomeNewProductMapper.deleteByExample(smsHomeNewProductExample);

    }

    @Override
    @Transactional
    public int updateSort(Long id, Integer sort) {
        SmsHomeNewProduct update = new SmsHomeNewProduct();
        update.setId(id);
        update.setSort(sort);
        return smsHomeNewProductMapper.updateByPrimaryKeySelective(update);

    }

    @Override
    @Transactional
    public int create(List<SmsHomeNewProduct> homeBrandList) {
        if (CollectionUtils.isEmpty(homeBrandList)) return 0;
        for (SmsHomeNewProduct newProduct : homeBrandList) {
            if (Objects.isNull(newProduct.getSort())) {
                newProduct.setSort(0);
            }
        }
        return smsHomeNewProductDao.insertList(homeBrandList);
    }

    @Override
    public List<PmsProduct> hotList(String productName, Integer pageNum, Integer pageSize) {
        int page = (pageNum - 1) * pageSize;
        return smsHomeNewProductDao.hotList(productName, page, pageSize);
    }

    @Override
    public List<PmsProduct> appList(String productName, Integer pageNum, Integer pageSize) {
        int page = (pageNum - 1) * pageSize;
        return smsHomeNewProductDao.appList(productName, page, pageSize);
    }
}
