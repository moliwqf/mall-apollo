package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.moli.mall.admin.dao.PmsSkuStockDao;
import com.moli.mall.admin.service.PmsSkuStockService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.PmsProductMapper;
import com.moli.mall.mbg.mapper.PmsSkuStockMapper;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductExample;
import com.moli.mall.mbg.model.PmsSkuStock;
import com.moli.mall.mbg.model.PmsSkuStockExample;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.Struct;
import java.util.Collection;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-03 10:27:37
 * @description 商品库存服务实现层
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {

    @Resource
    private PmsSkuStockMapper pmsSkuStockMapper;

    @Resource
    private PmsProductMapper pmsProductMapper;

    @Resource
    private PmsSkuStockDao pmsSkuStockDao;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample pmsSkuStockExample = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = pmsSkuStockExample.createCriteria();
        criteria.andProductIdEqualTo(pid);
        if (StrUtil.isNotEmpty(keyword)) {
            criteria.andSkuCodeLike("%" + keyword + "%");
        }
        return pmsSkuStockMapper.selectByExample(pmsSkuStockExample);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        // 查询是否存在该商品
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.createCriteria().andIdEqualTo(pid).andDeleteStatusEqualTo(0);
        List<PmsProduct> pmsProducts = pmsProductMapper.selectByExample(pmsProductExample);
        if (CollectionUtils.isEmpty(pmsProducts)) {
            AssetsUtil.fail("商品不存在!!");
        }
        return pmsSkuStockDao.replaceList(skuStockList);
    }
}
