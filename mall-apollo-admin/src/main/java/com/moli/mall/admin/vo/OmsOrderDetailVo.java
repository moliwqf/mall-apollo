package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.OmsOrder;
import com.moli.mall.mbg.model.OmsOrderItem;
import com.moli.mall.mbg.model.OmsOrderOperateHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 11:03:27
 * @description 订单详情信息
 */
public class OmsOrderDetailVo extends OmsOrder {

    @Getter
    @Setter
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;

    @Getter
    @Setter
    @ApiModelProperty("订单操作记录列表")
    private List<OmsOrderOperateHistory> historyList;
}
