package com.moli.mall.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.mbg.mapper.PmsBrandMapper;
import com.moli.mall.mbg.mapper.PmsProductMapper;
import com.moli.mall.mbg.model.PmsBrand;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductExample;
import com.moli.mall.portal.dao.HomeDao;
import com.moli.mall.portal.service.PmsPortalBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-07 10:06:54
 * @description 前台品牌管理
 */
@Service
public class PmsPortalBrandServiceImpl implements PmsPortalBrandService {

    @Resource
    private PmsBrandMapper brandMapper;

    @Resource
    private PmsProductMapper productMapper;

    @Resource
    private HomeDao homeDao;

    @Override
    public CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andBrandIdEqualTo(brandId).andDeleteStatusEqualTo(0);
        List<PmsProduct> pmsProducts = productMapper.selectByExample(pmsProductExample);
        return CommonPage.restPage(pmsProducts);
    }

    @Override
    public PmsBrand detail(Long brandId) {
        return brandMapper.selectByPrimaryKey(brandId);
    }

    @Override
    public List<PmsBrand> recommendList(Integer pageNum, Integer pageSize) {
        return homeDao.getRecommendBrandList((pageNum - 1) * pageSize, pageSize);
    }
}
