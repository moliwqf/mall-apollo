package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.PmsProductCategoryService;
import com.moli.mall.admin.vo.PmsProductCategoryWithChildrenVo;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.PmsProductCategoryMapper;
import com.moli.mall.mbg.model.PmsProductCategory;
import com.moli.mall.mbg.model.PmsProductCategoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:15:12
 * @description 产品分类服务实现层
 */
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Resource
    private PmsProductCategoryMapper pmsProductCategoryMapper;

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
}
