package com.moli.mall.portal.vo;

import com.moli.mall.mbg.model.UmsIntegrationConsumeSetting;
import com.moli.mall.mbg.model.UmsMemberReceiveAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-07 13:51:52
 * @description 确认单信息封装
 */
@Data
public class ConfirmOrderVo {

    //包含优惠信息的购物车信息
    private List<CartPromotionItemVo> cartPromotionItemList;
    //用户收货地址列表
    private List<UmsMemberReceiveAddress> memberReceiveAddressList;
    //用户可用优惠券列表
    private List<SmsCouponHistoryDetailVo> couponHistoryDetailList;
    //积分使用规则
    private UmsIntegrationConsumeSetting integrationConsumeSetting;
    //会员持有的积分
    private Integer memberIntegration;
    //计算的金额
    private CalcAmount calcAmount;

    @Data
    public static class CalcAmount{
        //订单商品总金额
        private BigDecimal totalAmount;
        //运费
        private BigDecimal freightAmount;
        //活动优惠
        private BigDecimal promotionAmount;
        //应付金额
        private BigDecimal payAmount;
    }
}
