package com.moli.mall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.PmsProductAttributeDao;
import com.moli.mall.admin.service.PmsProductAttributeCategoryService;
import com.moli.mall.admin.service.PmsProductAttributeService;
import com.moli.mall.admin.vo.PmsProductAttributeCategoryVo;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.moli.mall.mbg.mapper.PmsProductAttributeMapper;
import com.moli.mall.mbg.model.PmsProductAttribute;
import com.moli.mall.mbg.model.PmsProductAttributeCategory;
import com.moli.mall.mbg.model.PmsProductAttributeCategoryExample;
import com.moli.mall.mbg.model.PmsProductAttributeExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-02 15:56:11
 * @description 产品属性分类服务实现层
 */
@Service
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    @Resource
    private PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;

    @Resource
    private PmsProductAttributeMapper pmsProductAttributeMapper;

    @Resource
    private PmsProductAttributeDao pmsProductAttributeDao;

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return pmsProductAttributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }

    @Override
    public List<PmsProductAttributeCategoryVo> getListWithAttr() {
        return pmsProductAttributeDao.getListWithAttr();
    }

    @Override
    @Transactional
    public int create(String name) {
        PmsProductAttributeCategory add = new PmsProductAttributeCategory();
        add.setName(name);
        add.setAttributeCount(0);
        add.setParamCount(0);
        return pmsProductAttributeCategoryMapper.insert(add);
    }

    @Override
    @Transactional
    public int update(Long id, String name) {
        PmsProductAttributeCategory raw = pmsProductAttributeCategoryMapper.selectByPrimaryKey(id);
        if (Objects.isNull(raw)) {
            AssetsUtil.fail("不存在该商品类型");
        }
        PmsProductAttributeCategory update = new PmsProductAttributeCategory();
        update.setId(id);
        update.setName(name);
        return pmsProductAttributeCategoryMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        // 判断是否有产品属性包含该属性类型
        PmsProductAttributeExample pmsProductAttributeExample = new PmsProductAttributeExample();
        pmsProductAttributeExample.createCriteria().andProductAttributeCategoryIdEqualTo(id);
        List<PmsProductAttribute> attrs = pmsProductAttributeMapper.selectByExample(pmsProductAttributeExample);
        if (!CollectionUtils.isEmpty(attrs)) {
            AssetsUtil.fail("该商品类型有相关属性，删除之前请修改相关属性！！");
        }
        // 删除当前类型
        return pmsProductAttributeCategoryMapper.deleteByPrimaryKey(id);
    }
}
