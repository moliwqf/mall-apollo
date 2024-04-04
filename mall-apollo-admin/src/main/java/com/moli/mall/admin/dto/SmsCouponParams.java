package com.moli.mall.admin.dto;

import com.moli.mall.mbg.model.SmsCoupon;
import com.moli.mall.mbg.model.SmsCouponProductCategoryRelation;
import com.moli.mall.mbg.model.SmsCouponProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 00:00:33
 * @description 优惠券信息封装，包括绑定商品和绑定分类
 */
public class SmsCouponParams extends SmsCoupon {

    @Getter
    @Setter
    @ApiModelProperty("优惠券绑定的商品")
    private List<SmsCouponProductRelation> productRelationList;

    @Getter
    @Setter
    @ApiModelProperty("优惠券绑定的商品分类")
    private List<SmsCouponProductCategoryRelation> productCategoryRelationList;
}
