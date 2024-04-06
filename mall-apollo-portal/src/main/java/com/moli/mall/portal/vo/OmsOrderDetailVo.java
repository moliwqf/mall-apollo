package com.moli.mall.portal.vo;

import com.moli.mall.mbg.model.OmsOrder;
import com.moli.mall.mbg.model.OmsOrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 16:46:09
 * @description 包含订单商品信息的订单详情
 */
public class OmsOrderDetailVo extends OmsOrder {
    @Getter
    @Setter
    @ApiModelProperty("订单项目信息")
    private List<OmsOrderItem> orderItemList;
}
