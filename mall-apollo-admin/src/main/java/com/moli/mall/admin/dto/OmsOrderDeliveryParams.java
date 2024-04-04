package com.moli.mall.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author moli
 * @time 2024-04-04 14:51:09
 * @description 订单发货参数
 */
@Data
public class OmsOrderDeliveryParams {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("物流公司")
    private String deliveryCompany;

    @ApiModelProperty("物流单号")
    private String deliverySn;
}
