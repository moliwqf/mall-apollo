package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dto.PmsBrandParams;
import com.moli.mall.admin.service.PmsBrandService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.PmsBrandMapper;
import com.moli.mall.mbg.mapper.PmsProductMapper;
import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.PmsBrandExample;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-02 15:34:00
 * @description 品牌服务实现层
 */
@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Resource
    private PmsBrandMapper pmsBrandMapper;

    @Resource
    private PmsProductMapper pmsProductMapper;

    @Override
    public List<PmsBrand> listBrand(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.setOrderByClause("sort desc");
        if (StrUtil.isNotEmpty(keyword)) {
            pmsBrandExample.createCriteria().andNameLike("%" + keyword + "%");
        }
        return pmsBrandMapper.selectByExample(pmsBrandExample);
    }

    @Override
    public PmsBrand info(Long id) {
        return pmsBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int createBrand(PmsBrandParams pmsBrand) {
        PmsBrand addBrand = BeanCopyUtil.copyBean(pmsBrand, PmsBrand.class);
        assert addBrand != null;
        addBrand.setProductCount(0);
        addBrand.setProductCommentCount(0);
        return pmsBrandMapper.insertSelective(addBrand);
    }

    @Override
    @Transactional
    public int updateBrand(Long id, PmsBrandParams pmsBrandParam) {
        // 查询是否存在该品牌
        PmsBrand rawBrand = pmsBrandMapper.selectByPrimaryKey(id);
        if (Objects.isNull(rawBrand)) {
            AssetsUtil.fail("不能存在该品牌");
        }
        PmsBrand updateBrand = BeanCopyUtil.copyBean(pmsBrandParam, PmsBrand.class);
        assert updateBrand != null;
        updateBrand.setId(id);
        return pmsBrandMapper.updateByPrimaryKeySelective(updateBrand);
    }

    @Override
    @Transactional
    public int deleteBrand(Long id) {
        // 查询是否存在商品属于该品牌
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andBrandIdEqualTo(id).andDeleteStatusEqualTo(0);
        List<PmsProduct> pmsProducts = pmsProductMapper.selectByExample(pmsProductExample);
        if (!CollectionUtils.isEmpty(pmsProducts)) {
            AssetsUtil.fail("该品牌还有商品，请先删除相关商品！！");
        }

        return pmsBrandMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        PmsBrand update = new PmsBrand();
        update.setShowStatus(showStatus);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.createCriteria().andIdIn(ids);
        return pmsBrandMapper.updateByExampleSelective(update, pmsBrandExample);
    }

    @Override
    @Transactional
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        PmsBrand update = new PmsBrand();
        update.setFactoryStatus(factoryStatus);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.createCriteria().andIdIn(ids);
        return pmsBrandMapper.updateByExampleSelective(update, pmsBrandExample);
    }
}
