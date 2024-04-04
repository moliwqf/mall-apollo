package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.SmsCouponProductCategoryRelationDao;
import com.moli.mall.admin.dao.SmsCouponProductRelationDao;
import com.moli.mall.admin.dto.SmsCouponParams;
import com.moli.mall.admin.service.SmsCouponService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.SmsCouponMapper;
import com.moli.mall.mbg.mapper.SmsCouponProductCategoryRelationMapper;
import com.moli.mall.mbg.mapper.SmsCouponProductRelationMapper;
import com.moli.mall.mbg.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author moli
 * @time 2024-04-04 23:59:35
 * @description 优惠券管理
 */
@Service
public class SmsCouponServiceImpl implements SmsCouponService {

    @Resource
    private SmsCouponMapper smsCouponMapper;

    @Resource
    private SmsCouponProductRelationMapper smsCouponProductRelationMapper;

    @Resource
    private SmsCouponProductRelationDao smsCouponProductRelationDao;

    @Resource
    private SmsCouponProductCategoryRelationMapper smsCouponProductCategoryRelationMapper;

    @Resource
    private SmsCouponProductCategoryRelationDao smsCouponProductCategoryRelationDao;

    @Override
    public SmsCouponParams getItem(Long id) {
        // 获取基础信息
        SmsCoupon smsCoupon = smsCouponMapper.selectByPrimaryKey(id);
        if (Objects.isNull(smsCoupon)) {
            AssetsUtil.fail("查询优惠券失败!!");
        }
        SmsCouponParams result = BeanCopyUtil.copyBean(smsCoupon, SmsCouponParams.class);
        assert result != null;

        // 查询优惠券绑定的商品
        CompletableFuture<List<SmsCouponProductRelation>> relationFuture = CompletableFuture.supplyAsync(() -> {
            SmsCouponProductRelationExample smsCouponProductRelationExample = new SmsCouponProductRelationExample();
            smsCouponProductRelationExample.createCriteria().andCouponIdEqualTo(id);
            return smsCouponProductRelationMapper.selectByExample(smsCouponProductRelationExample);
        }).whenComplete((productRelationList, e) -> result.setProductRelationList(productRelationList));

        // 查询优惠券绑定的商品分类
        SmsCouponProductCategoryRelationExample smsCouponProductCategoryRelationExample = new SmsCouponProductCategoryRelationExample();
        smsCouponProductCategoryRelationExample.createCriteria().andCouponIdEqualTo(id);
        List<SmsCouponProductCategoryRelation> smsCouponProductCategoryRelations = smsCouponProductCategoryRelationMapper.selectByExample(smsCouponProductCategoryRelationExample);
        result.setProductCategoryRelationList(smsCouponProductCategoryRelations);

        try {
            relationFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("查询优惠券绑定的商品失败！！");
        }

        return result;
    }

    @Override
    public List<SmsCoupon> list(String name, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsCouponExample smsCouponExample = new SmsCouponExample();
        smsCouponExample.setOrderByClause("start_time asc, enable_time desc");
        SmsCouponExample.Criteria criteria = smsCouponExample.createCriteria();

        if (StrUtil.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }

        if (Objects.nonNull(type)) {
            criteria.andTypeEqualTo(type);
        }

        return smsCouponMapper.selectByExample(smsCouponExample);
    }

    @Override
    @Transactional
    public int update(Long id, SmsCouponParams couponParam) {
        if (id <= 0) AssetsUtil.fail("非法id，禁止访问!!");
        SmsCoupon rawObj = smsCouponMapper.selectByPrimaryKey(id);
        if (Objects.isNull(rawObj)) {
            return 0;
        }

        // 更新优惠券绑定的商品
        CompletableFuture<Void> relationFuture = CompletableFuture.runAsync(() -> {
            // 删除之前的关系
            SmsCouponProductRelationExample smsCouponProductRelationExample = new SmsCouponProductRelationExample();
            smsCouponProductRelationExample.createCriteria().andCouponIdEqualTo(id);
            smsCouponProductRelationMapper.deleteByExample(smsCouponProductRelationExample);
            // 绑定新的关系
            List<SmsCouponProductRelation> productRelationList = couponParam.getProductRelationList();
            if (!CollectionUtils.isEmpty(productRelationList)) {
                for (SmsCouponProductRelation relation : productRelationList) {
                    relation.setCouponId(id);
                }
                smsCouponProductRelationDao.insertList(productRelationList);
            }
        });

        // 更新优惠券绑定的商品分类
        CompletableFuture<Void> cateRelationFuture = CompletableFuture.runAsync(() -> {
            // 删除之前的关系
            SmsCouponProductCategoryRelationExample smsCouponProductCategoryRelationExample = new SmsCouponProductCategoryRelationExample();
            smsCouponProductCategoryRelationExample.createCriteria().andCouponIdEqualTo(id);
            smsCouponProductCategoryRelationMapper.deleteByExample(smsCouponProductCategoryRelationExample);

            // 绑定新的关系
            List<SmsCouponProductCategoryRelation> productCategoryRelationList = couponParam.getProductCategoryRelationList();
            if (!CollectionUtils.isEmpty(productCategoryRelationList)) {
                for (SmsCouponProductCategoryRelation relation : productCategoryRelationList) {
                    relation.setCouponId(id);
                }
                smsCouponProductCategoryRelationDao.insertList(productCategoryRelationList);
            }
        });

        SmsCoupon update = BeanCopyUtil.copyBean(couponParam, SmsCoupon.class);
        assert update != null;
        update.setId(id);
        int count = smsCouponMapper.updateByPrimaryKey(update);

        try {
            CompletableFuture.allOf(relationFuture, cateRelationFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("更新失败");
        }

        return count;
    }

    @Override
    @Transactional
    public int delete(Long id) {
        if (id <= 0) AssetsUtil.fail("非法id，禁止访问!!");
        return smsCouponMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int create(SmsCouponParams couponParam) {
        // 添加优惠券
        int count = smsCouponMapper.insert(couponParam);

        Long id = couponParam.getId();
        // 添加优惠券绑定的商品
        CompletableFuture<Void> relationFuture = CompletableFuture.runAsync(() -> {
            List<SmsCouponProductRelation> productRelationList = couponParam.getProductRelationList();
            if (!CollectionUtils.isEmpty(productRelationList)) {
                for (SmsCouponProductRelation relation : productRelationList) {
                    relation.setCouponId(id);
                }
                smsCouponProductRelationDao.insertList(productRelationList);
            }
        });

        // 添加优惠券绑定的商品分类
        CompletableFuture<Void> cateRelationFuture = CompletableFuture.runAsync(() -> {
            List<SmsCouponProductCategoryRelation> productCategoryRelationList = couponParam.getProductCategoryRelationList();
            if (!CollectionUtils.isEmpty(productCategoryRelationList)) {
                for (SmsCouponProductCategoryRelation relation : productCategoryRelationList) {
                    relation.setCouponId(id);
                }
                smsCouponProductCategoryRelationDao.insertList(productCategoryRelationList);
            }
        });

        try {
            CompletableFuture.allOf(relationFuture, cateRelationFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("添加优惠券失败");
        }

        return count;
    }
}
