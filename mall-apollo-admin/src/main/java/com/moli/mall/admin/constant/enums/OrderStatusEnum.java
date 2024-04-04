package com.moli.mall.admin.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author moli
 * @time 2024-04-04 14:56:25
 * @description 订单状态
 */
@AllArgsConstructor
@Getter
public enum OrderStatusEnum {

    NOT_PAYED(0, "未付款"),

    NOT_SHIPPED(1, "待发货"),

    SHIPPING(2, "已发货"),

    FINISHED(3, "已完成"),

    CLOSED(4, "已关闭"),

    INVALID_ORDER(5, "无效订单");

    private final Integer status;

    private final String desc;
}
