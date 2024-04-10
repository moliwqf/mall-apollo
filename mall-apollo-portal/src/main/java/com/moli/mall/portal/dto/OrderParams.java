package com.moli.mall.portal.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-08 15:12:09
 * @description 生成订单时传入的参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderParams {

    @ApiModelProperty("收货地址ID")
    private Long memberReceiveAddressId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("使用的积分数")
    private Integer useIntegration;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("被选中的购物车商品ID")
    private List<Long> cartIds;
}
