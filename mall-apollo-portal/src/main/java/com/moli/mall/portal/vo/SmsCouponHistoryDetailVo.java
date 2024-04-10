package com.moli.mall.portal.vo;

import com.moli.mall.mbg.model.SmsCoupon;
import com.moli.mall.mbg.model.SmsCouponHistory;
import com.moli.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.moli.mall.mbg.model.SmsCouponProductRelation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-07 13:53:36
 * @description 优惠券领取历史详情封装
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsCouponHistoryDetailVo extends SmsCouponHistory {
    //相关优惠券信息
    private SmsCoupon coupon;
    //优惠券关联商品
    private List<SmsCouponProductRelation> productRelationList;
    //优惠券关联商品分类
    private List<SmsCouponProductCategoryRelation> categoryRelationList;
}
