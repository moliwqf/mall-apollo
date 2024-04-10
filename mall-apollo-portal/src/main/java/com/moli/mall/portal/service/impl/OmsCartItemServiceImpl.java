package com.moli.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.mapper.OmsCartItemMapper;
import com.moli.mall.mbg.model.OmsCartItem;
import com.moli.mall.mbg.model.OmsCartItemExample;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.service.OmsCartItemService;
import com.moli.mall.portal.service.OmsPromotionService;
import com.moli.mall.portal.service.UmsMemberService;
import com.moli.mall.portal.vo.CartPromotionItemVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-06 23:04:30
 * @description 购物车管理
 */
@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {

    @Resource
    private OmsCartItemMapper omsCartItemMapper;

    @Resource
    private UmsMemberService umsMemberService;

    @Resource
    private OmsPromotionService promotionService;

    @Override
    public List<OmsCartItem> list() {
        UmsMember current = umsMemberService.info();
        return list(current.getId());
    }

    @Override
    @Transactional
    public int add(OmsCartItem cartItem) {
        UmsMember current = umsMemberService.info();
        cartItem.setMemberId(current.getId());
        cartItem.setMemberNickname(current.getNickname());
        cartItem.setDeleteStatus(0);

        OmsCartItem target = getCartItem(cartItem);
        if (Objects.isNull(target)) {
            cartItem.setCreateDate(new Date());
            return omsCartItemMapper.insert(cartItem);
        } else {
            target.setModifyDate(new Date());
            target.setQuantity(cartItem.getQuantity());
            return omsCartItemMapper.updateByPrimaryKey(target);
        }
    }

    @Override
    @Transactional
    public int clear() {
        UmsMember current = umsMemberService.info();
        OmsCartItemExample omsCartItemExample = new OmsCartItemExample();
        omsCartItemExample.createCriteria().andMemberIdEqualTo(current.getId());
        return omsCartItemMapper.deleteByExample(omsCartItemExample);
    }

    @Override
    @Transactional
    public int updateQuantity(Long id, Integer quantity) {
        UmsMember current = umsMemberService.info();
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setQuantity(quantity);

        OmsCartItemExample omsCartItemExample = new OmsCartItemExample();
        omsCartItemExample.createCriteria().andMemberIdEqualTo(current.getId()).andIdEqualTo(id);
        return omsCartItemMapper.updateByExampleSelective(omsCartItem, omsCartItemExample);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        UmsMember current = umsMemberService.info();
        return delete(ids, current.getId());
    }

    @Override
    @Transactional
    public int delete(List<Long> ids, Long memberId) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample omsCartItemExample = new OmsCartItemExample();
        omsCartItemExample.createCriteria().andMemberIdEqualTo(memberId).andIdIn(ids);
        return omsCartItemMapper.updateByExampleSelective(record, omsCartItemExample);
    }

    @Override
    public List<CartPromotionItemVo> listPromotion(Long memberId, List<Long> cartIds) {
        List<OmsCartItem> cartItemList = list(memberId);
        if (CollUtil.isNotEmpty(cartIds)) {
            cartItemList = cartItemList.stream().filter(item -> cartIds.contains(item.getId())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(cartItemList)) {
            return new ArrayList<>();
        }
        return promotionService.calcCartPromotion(cartItemList);
    }

    /**
     * 根据用户id获取用户购物车信息
     */
    private List<OmsCartItem> list(Long memberId) {
        OmsCartItemExample omsCartItemExample = new OmsCartItemExample();
        omsCartItemExample.createCriteria().andMemberIdEqualTo(memberId).andDeleteStatusEqualTo(0);
        return omsCartItemMapper.selectByExample(omsCartItemExample);
    }

    /**
     * 根据会员id、商品id、商品sku id获取购物车项
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample omsCartItemExample = new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = omsCartItemExample.createCriteria().andMemberIdEqualTo(cartItem.getMemberId())
                .andDeleteStatusEqualTo(0)
                .andProductIdEqualTo(cartItem.getProductId());
        if (Objects.nonNull(cartItem.getProductSkuId())) {
            criteria.andProductSkuIdEqualTo(cartItem.getProductId());
        }
        List<OmsCartItem> omsCartItems = omsCartItemMapper.selectByExample(omsCartItemExample);
        if (CollectionUtils.isEmpty(omsCartItems)) return null;
        return omsCartItems.get(0);
    }
}
