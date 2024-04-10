package com.moli.mall.portal.service.impl;

import com.moli.mall.mbg.model.SmsCoupon;
import com.moli.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.moli.mall.mbg.model.SmsCouponProductRelation;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.dao.SmsCouponHistoryDao;
import com.moli.mall.portal.service.UmsMemberCouponService;
import com.moli.mall.portal.service.UmsMemberService;
import com.moli.mall.portal.vo.CartPromotionItemVo;
import com.moli.mall.portal.vo.SmsCouponHistoryDetailVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-06 15:45:57
 * @description 用户优惠券管理
 */
@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {

    @Resource
    private UmsMemberService umsMemberService;

    @Resource
    private SmsCouponHistoryDao smsCouponHistoryDao;

    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        // 获取当前用户信息
        UmsMember currentUser = umsMemberService.info();
        return smsCouponHistoryDao.listHistories(currentUser.getId(), useStatus);
    }

    @Override
    public List<SmsCouponHistoryDetailVo> listCoupons(List<CartPromotionItemVo> cartPromotionItemList, int type) {
        UmsMember currentUser = umsMemberService.info();
        Date now = new Date();
        // 获取当前用户的所有优惠券
        List<SmsCouponHistoryDetailVo> allList = smsCouponHistoryDao.getDetailList(currentUser.getId());
        // 根据优惠券情况判断优惠券是否可用
        List<SmsCouponHistoryDetailVo> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetailVo> disableList = new ArrayList<>();

        for (SmsCouponHistoryDetailVo detailVo : allList) {
            Integer couponUseType = detailVo.getCoupon().getUseType();
            // 优惠券使用门槛
            BigDecimal minPoint = detailVo.getCoupon().getMinPoint();
            Date endTime = detailVo.getCoupon().getEndTime();

            if (couponUseType == 0) {
                // 全场通用
                // 计算购物项总金额
                BigDecimal totalAmount = calcTotalAmount(cartPromotionItemList);

                if (now.before(endTime) && totalAmount.compareTo(minPoint) >= 0) {
                    enableList.add(detailVo);
                } else {
                    disableList.add(detailVo);
                }
            } else if (couponUseType == 1) {
                // 指定分类使用
                // 计算指定分类下的商品总价
                Set<Long> productCategoryIds = detailVo.getCategoryRelationList()
                        .stream()
                        .map(SmsCouponProductCategoryRelation::getProductCategoryId)
                        .collect(Collectors.toSet());
                BigDecimal totalAmount = calcTotalAmountByProductCategoryIds(productCategoryIds, cartPromotionItemList);

                if (now.before(endTime) && totalAmount.compareTo(minPoint) >= 0) {
                    enableList.add(detailVo);
                } else {
                    disableList.add(detailVo);
                }
            } else if (couponUseType == 2) {
                // 指定商品使用
                // 计算指定商品的总价
                Set<Long> productIds = detailVo.getProductRelationList()
                        .stream()
                        .map(SmsCouponProductRelation::getProductId)
                        .collect(Collectors.toSet());
                BigDecimal totalAmount = calcTotalAmountByProductIds(productIds, cartPromotionItemList);

                if (now.before(endTime) && totalAmount.compareTo(minPoint) >= 0) {
                    enableList.add(detailVo);
                } else {
                    disableList.add(detailVo);
                }
            }
        }

        if (type == 1) {
            return enableList;
        } else {
            return disableList;
        }
    }

    /**
     * 计算指定商品下的总价
     * @param productIds 指定商品id
     * @param itemList 商品项
     * @return 总价
     */
    private BigDecimal calcTotalAmountByProductIds(Set<Long> productIds, List<CartPromotionItemVo> itemList) {
        BigDecimal totalAmount = new BigDecimal(0);
        for (CartPromotionItemVo item : itemList) {
            Long productId = item.getProductId();
            if (productIds.contains(productId)) {
                BigDecimal realAmount = item.getPrice().subtract(item.getReduceAmount());
                totalAmount = totalAmount.add(realAmount.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return totalAmount;
    }

    /**
     * 计算指定商品分类下的商品总金额
     * @param productCategoryIds 指定商品分类id
     * @param itemList 购物车项
     * @return 总金额
     */
    private BigDecimal calcTotalAmountByProductCategoryIds(Set<Long> productCategoryIds, List<CartPromotionItemVo> itemList) {
        BigDecimal totalAmount = new BigDecimal(0);
        for (CartPromotionItemVo item : itemList) {
            Long currentCategoryId = item.getProductCategoryId();
            if (productCategoryIds.contains(currentCategoryId)) {
                BigDecimal realAmount = item.getPrice().subtract(item.getReduceAmount());
                totalAmount = totalAmount.add(realAmount.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return totalAmount;
    }

    /**
     * 计算购物车总金额
     */
    private BigDecimal calcTotalAmount(List<CartPromotionItemVo> itemList) {
        BigDecimal totalAmount = new BigDecimal(0);
        for (CartPromotionItemVo item : itemList) {
            BigDecimal realAmount = item.getPrice().subtract(item.getReduceAmount());
            totalAmount = totalAmount.add(realAmount.multiply(new BigDecimal(item.getQuantity())));
        }
        return totalAmount;
    }
}
