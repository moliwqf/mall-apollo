package com.moli.mall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.PmsProductCategoryAttributeRelationDao;
import com.moli.mall.admin.dto.PmsProductCategoryParams;
import com.moli.mall.admin.service.PmsProductCategoryService;
import com.moli.mall.admin.vo.PmsProductCategoryWithChildrenVo;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.PmsProductCategoryAttributeRelationMapper;
import com.moli.mall.mbg.mapper.PmsProductCategoryMapper;
import com.moli.mall.mbg.model.PmsProductCategory;
import com.moli.mall.mbg.model.PmsProductCategoryAttributeRelation;
import com.moli.mall.mbg.model.PmsProductCategoryAttributeRelationExample;
import com.moli.mall.mbg.model.PmsProductCategoryExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-02 15:15:12
 * @description 产品分类服务实现层
 */
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Resource
    private PmsProductCategoryMapper pmsProductCategoryMapper;

    @Resource
    private PmsProductCategoryAttributeRelationMapper pmsProductCategoryAttributeRelationMapper;

    @Resource
    private PmsProductCategoryAttributeRelationDao pmsProductCategoryAttributeRelationDao;

    @Override
    public List<PmsProductCategoryWithChildrenVo> listWithChildren() {
        return listChildren(0L);
    }

    /**
     * 根据父分类id获取子分类
     *
     * @param parentId 父分类id
     * @return 分类信息
     */
    private List<PmsProductCategoryWithChildrenVo> listChildren(Long parentId) {
        // 查询子分类
        PmsProductCategoryExample pmsProductCategoryExample = new PmsProductCategoryExample();
        pmsProductCategoryExample.createCriteria().andParentIdEqualTo(parentId);
        List<PmsProductCategory> categories = pmsProductCategoryMapper.selectByExample(pmsProductCategoryExample);
        if (CollectionUtils.isEmpty(categories)) {
            return new ArrayList<>();
        }
        // 查询子子分类
        List<PmsProductCategoryWithChildrenVo> finalCategories = BeanCopyUtil.copyBeanList(categories, PmsProductCategoryWithChildrenVo.class);

        for (PmsProductCategoryWithChildrenVo category : finalCategories) {
            List<PmsProductCategoryWithChildrenVo> child = listChildren(category.getId());
            category.setChildren(child);
        }

        return finalCategories;
    }

    @Override
    public List<PmsProductCategory> getList(Long parentId, Integer pageNum, Integer pageSize) {
        if (Objects.isNull(parentId)) {
            AssetsUtil.fail("parentId为null");
        }
        PageHelper.startPage(pageNum, pageSize);
        PmsProductCategoryExample pmsProductCategoryExample = new PmsProductCategoryExample();
        pmsProductCategoryExample.createCriteria().andParentIdEqualTo(parentId);
        pmsProductCategoryExample.setOrderByClause("sort desc");
        return pmsProductCategoryMapper.selectByExample(pmsProductCategoryExample);
    }

    @Override
    public PmsProductCategory info(Long id) {
        return pmsProductCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int create(PmsProductCategoryParams productCategoryParam) {
        PmsProductCategory pmsProductCategory = BeanCopyUtil.copyBean(productCategoryParam, PmsProductCategory.class);
        assert pmsProductCategory != null;

        pmsProductCategory.setProductCount(0);

        setCategoryLevel(pmsProductCategory);

        int count = pmsProductCategoryMapper.insertSelective(pmsProductCategory);

        // 如果有属性关联，添加关联信息
        List<Long> productAttributeIdList = productCategoryParam.getProductAttributeIdList();
        if (!CollectionUtils.isEmpty(productAttributeIdList)) {
            List<PmsProductCategoryAttributeRelation> addRelationList = productAttributeIdList.stream()
                    .map(id -> new PmsProductCategoryAttributeRelation(pmsProductCategory.getId(), id))
                    .collect(Collectors.toList());

            pmsProductCategoryAttributeRelationDao.insertList(addRelationList);
        }

        return count;
    }

    @Override
    @Transactional
    public int update(Long id, PmsProductCategoryParams productCategoryParam) {
        // 查询是否存在该分类
        PmsProductCategory rawCategory = pmsProductCategoryMapper.selectByPrimaryKey(id);
        if (Objects.isNull(rawCategory)) {
            AssetsUtil.fail("不存在该产品分类信息");
        }
        PmsProductCategory newProductCategory = BeanCopyUtil.copyBean(productCategoryParam, PmsProductCategory.class);
        assert newProductCategory != null;
        newProductCategory.setId(id);

        setCategoryLevel(newProductCategory);

        // 删除之前的属性关联信息
        PmsProductCategoryAttributeRelationExample categoryAttr = new PmsProductCategoryAttributeRelationExample();
        categoryAttr.createCriteria().andProductCategoryIdEqualTo(id);
        pmsProductCategoryAttributeRelationMapper.deleteByExample(categoryAttr);

        // 添加新的属性关联信息
        List<Long> productAttributeIdList = productCategoryParam.getProductAttributeIdList();
        if (!CollectionUtils.isEmpty(productAttributeIdList)) {
            List<PmsProductCategoryAttributeRelation> addRelationList = productAttributeIdList.stream()
                    .map(paId -> new PmsProductCategoryAttributeRelation(id, paId))
                    .collect(Collectors.toList());

            pmsProductCategoryAttributeRelationDao.insertList(addRelationList);
        }

        return pmsProductCategoryMapper.updateByPrimaryKeySelective(newProductCategory);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        // 删除与该分类关联的属性
        CompletableFuture<Void> delRelations = CompletableFuture.runAsync(() -> {
            PmsProductCategoryAttributeRelationExample categoryAttr = new PmsProductCategoryAttributeRelationExample();
            categoryAttr.createCriteria().andProductCategoryIdEqualTo(id);
            pmsProductCategoryAttributeRelationMapper.deleteByExample(categoryAttr);
        });

        // 删除该分类
        CompletableFuture<Integer> delCate = CompletableFuture.supplyAsync(() -> pmsProductCategoryMapper.deleteByPrimaryKey(id));

        try {
            CompletableFuture.allOf(delCate, delRelations).get();
            return delCate.get();
        } catch (InterruptedException | ExecutionException e) {
            AssetsUtil.fail("异步删除失败");
        }
        return 0;
    }

    @Override
    public List<PmsProductCategory> getAll(Long parentId) {
        PmsProductCategoryExample pmsProductCategoryExample = new PmsProductCategoryExample();
        pmsProductCategoryExample.createCriteria()
                .andParentIdEqualTo(parentId)
                .andShowStatusEqualTo(1);
        pmsProductCategoryExample.setOrderByClause("sort desc");
        return pmsProductCategoryMapper.selectByExample(pmsProductCategoryExample);
    }

    /**
     * 设置分类级别
     */
    private void setCategoryLevel(PmsProductCategory productCategory) {
        if (Objects.isNull(productCategory.getParentId())) {
            productCategory.setLevel(0);
        } else {
            // 查询父分类
            PmsProductCategory parent = pmsProductCategoryMapper.selectByPrimaryKey(productCategory.getParentId());
            if (Objects.isNull(parent)) {
                productCategory.setLevel(0);
            } else {
                productCategory.setLevel(parent.getLevel() + 1);
            }
        }
    }
}
