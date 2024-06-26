package com.moli.mall.portal.vo;

import com.moli.mall.mbg.model.OmsCartItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author moli
 * @time 2024-04-07 13:52:26
 * @description 购物车中促销信息的封装
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CartPromotionItemVo extends OmsCartItem {
    //促销活动信息
    private String promotionMessage;
    //促销活动减去的金额，针对每个商品
    private BigDecimal reduceAmount;
    //商品的真实库存（剩余库存-锁定库存）
    private Integer realStock;
    //购买商品赠送积分
    private Integer integration;
    //购买商品赠送成长值
    private Integer growth;
}
