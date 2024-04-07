package com.moli.mall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.SmsFlashPromotionProductRelationDao;
import com.moli.mall.admin.service.SmsFlashPromotionProductRelationService;
import com.moli.mall.admin.vo.FlashPromotionProduct;
import com.moli.mall.admin.vo.SmsFlashPromotionProductVo;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.PmsProductMapper;
import com.moli.mall.mbg.mapper.SmsFlashPromotionProductRelationMapper;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.PmsProductExample;
import com.moli.mall.mbg.model.SmsFlashPromotionProductRelation;
import com.moli.mall.mbg.model.SmsFlashPromotionProductRelationExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-05 14:29:27
 * @description 秒杀场次和商品联系服务
 */
@Service
public class SmsFlashPromotionProductRelationServiceImpl implements SmsFlashPromotionProductRelationService {

    @Resource
    private SmsFlashPromotionProductRelationMapper smsFlashPromotionProductRelationMapper;

    @Resource
    private SmsFlashPromotionProductRelationDao smsFlashPromotionProductRelationDao;

    @Resource
    private PmsProductMapper pmsProductMapper;

    @Override
    @Transactional
    public int create(List<SmsFlashPromotionProductRelation> relationList) {
        return smsFlashPromotionProductRelationDao.insertList(relationList);
    }

    @Override
    @Transactional
    public int update(Long id, SmsFlashPromotionProductRelation relation) {
        relation.setId(id);
        return smsFlashPromotionProductRelationMapper.updateByPrimaryKeySelective(relation);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        return smsFlashPromotionProductRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SmsFlashPromotionProductRelation getItem(Long id) {
        return smsFlashPromotionProductRelationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmsFlashPromotionProductVo> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsFlashPromotionProductRelationExample smsFlashPromotionProductRelationExample = new SmsFlashPromotionProductRelationExample();
        smsFlashPromotionProductRelationExample.setOrderByClause("sort desc");
        smsFlashPromotionProductRelationExample
                .createCriteria()
                .andFlashPromotionIdEqualTo(flashPromotionId)
                .andFlashPromotionSessionIdEqualTo(flashPromotionSessionId);
        List<SmsFlashPromotionProductRelation> smsFlashPromotionProductRelations = smsFlashPromotionProductRelationMapper.selectByExample(smsFlashPromotionProductRelationExample);

        List<SmsFlashPromotionProductVo> ret = new ArrayList<>();
        if (!CollectionUtils.isEmpty(smsFlashPromotionProductRelations)) {
            for (SmsFlashPromotionProductRelation relation : smsFlashPromotionProductRelations) {
                Long productId = relation.getProductId();
                if (Objects.isNull(productId)) continue;
                // 查询商品
                PmsProductExample pmsProductExample = new PmsProductExample();
                pmsProductExample.createCriteria().andIdEqualTo(productId).andDeleteStatusEqualTo(0);
                List<PmsProduct> pmsProducts = pmsProductMapper.selectByExample(pmsProductExample);
                if (CollectionUtils.isEmpty(pmsProducts)) continue;

                SmsFlashPromotionProductVo res = BeanCopyUtil.copyBean(relation, SmsFlashPromotionProductVo.class);
                assert res != null;
                res.setProduct(pmsProducts.get(0));
                ret.add(res);
            }
        }
        return ret;
    }

    @Override
    public List<FlashPromotionProduct> selectCurrentSessionProducts(Long promotionId, Long promotionSessionId) {
        // 获取当前场次下的所有商品
        SmsFlashPromotionProductRelationExample smsFlashPromotionProductRelationExample = new SmsFlashPromotionProductRelationExample();
        smsFlashPromotionProductRelationExample.setOrderByClause("sort desc");
        smsFlashPromotionProductRelationExample
                .createCriteria()
                .andFlashPromotionIdEqualTo(promotionId)
                .andFlashPromotionSessionIdEqualTo(promotionSessionId);
        List<SmsFlashPromotionProductRelation> smsFlashPromotionProductRelations = smsFlashPromotionProductRelationMapper.selectByExample(smsFlashPromotionProductRelationExample);

        List<FlashPromotionProduct> productList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(smsFlashPromotionProductRelations)) {
            for (SmsFlashPromotionProductRelation relation : smsFlashPromotionProductRelations) {
                Long productId = relation.getProductId();
                if (Objects.isNull(productId)) continue;
                // 查询商品
                PmsProductExample pmsProductExample = new PmsProductExample();
                pmsProductExample.createCriteria().andIdEqualTo(productId).andDeleteStatusEqualTo(0);
                List<PmsProduct> pmsProducts = pmsProductMapper.selectByExample(pmsProductExample);
                if (CollectionUtils.isEmpty(pmsProducts)) continue;

                FlashPromotionProduct res = BeanCopyUtil.copyBean(pmsProducts.get(0), FlashPromotionProduct.class);
                assert res != null;

                res.setFlashPromotionPrice(relation.getFlashPromotionPrice());
                res.setFlashPromotionCount(relation.getFlashPromotionCount());
                res.setFlashPromotionLimit(relation.getFlashPromotionLimit());

                productList.add(res);
            }
        }
        return productList;
    }
}
