package com.moli.mall.portal.service.impl;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.*;
import com.moli.mall.portal.service.*;
import com.moli.mall.portal.vo.HomeContentVo;
import com.moli.mall.portal.vo.HomeFlashPromotionVo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-05 20:14:38
 * @description 首页内容管理
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Resource
    private SmsHomeAdvertiseService smsHomeAdvertiseService;

    @Resource
    private SmsHomeBrandService smsHomeBrandService;

    @Resource
    private SmsFlashPromotionService smsFlashPromotionService;

    @Resource
    private SmsHomeNewProductService smsHomeNewProductService;

    @Resource
    private SmsHomeRecommendSubjectService smsHomeRecommendSubjectService;

    @Resource
    private PmsProductService pmsProductService;

    @Resource
    private PmsProductCategoryService pmsProductCategoryService;

    @Resource
    private CmsSubjectService cmsSubjectService;

    @Override
    public HomeContentVo content() {

        HomeContentVo content = new HomeContentVo();
        // 查询轮播广告
        List<SmsHomeAdvertise> smsHomeAdvertises = smsHomeAdvertiseService.appList();
        content.setAdvertiseList(smsHomeAdvertises);

        // 查询首页推荐品牌
        CommonResult<CommonPage<PmsBrand>> homeBrandList = smsHomeBrandService.appList(null, 1, 1, 6);
        content.setBrandList(homeBrandList.getData().getList());

        // 查询当前秒杀场次
        CommonResult<HomeFlashPromotionVo> result = smsFlashPromotionService.getCurrentFlashPromotion();
        if (Objects.nonNull(result.getData())) {
            content.setHomeFlashPromotion(result.getData());
        }

        // 查询新品推荐
        CommonResult<List<PmsProduct>> newProductsRecommend = smsHomeNewProductService.newProductsRecommend(null, 1, 1, 4);
        if (Objects.nonNull(newProductsRecommend.getData())) {
            content.setNewProductList(newProductsRecommend.getData());
        }

        // 查询人气推荐
        CommonResult<List<PmsProduct>> hotListPage = smsHomeNewProductService.hotList(null, 1, 4);
        if (Objects.nonNull(hotListPage.getData())) {
            content.setHotProductList(hotListPage.getData());
        }

        // 查询推荐专题
        CommonResult<List<CmsSubject>> recommendSubs = smsHomeRecommendSubjectService.appList(null, 1, 4);
        if (Objects.nonNull(recommendSubs.getData())) {
            content.setSubjectList(recommendSubs.getData());
        }

        return content;
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        CommonResult<CommonPage<PmsProduct>> productList = pmsProductService.recommendProductList(pageNum, pageSize);
        if (Objects.isNull(productList.getData())) return new ArrayList<>();
        return productList.getData().getList();
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        return pmsProductCategoryService.getListByParentId(parentId).getData();
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        CommonResult<CommonPage<CmsSubject>> subjectList = cmsSubjectService.getSubjectList(cateId, pageNum, pageSize);
        if (Objects.isNull(subjectList.getData())) return new ArrayList<>();
        return subjectList.getData().getList();
    }

    @Override
    public List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize) {
        CommonResult<List<PmsProduct>> hotListPage = smsHomeNewProductService.hotList(null, pageNum, pageSize);
        if (Objects.nonNull(hotListPage.getData())) {
            return hotListPage.getData();
        }
        return new ArrayList<>();
    }

    @Override
    public List<PmsProduct> newProductList(Integer pageNum, Integer pageSize) {
        CommonResult<List<PmsProduct>> newProductsRecommend = smsHomeNewProductService.newProductsRecommend(null, 1, pageNum, pageSize);
        if (Objects.nonNull(newProductsRecommend.getData())) {
            return newProductsRecommend.getData();
        }
        return new ArrayList<>();
    }
}
