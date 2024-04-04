package com.moli.mall.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author moli
 * @time 2024-04-04 17:15:30
 * @description 确认收货提交参数
 */
@Getter
@Setter
public class OmsUpdateStatusParams {

    @ApiModelProperty("服务单号")
    private Long id;

    @ApiModelProperty("收货地址关联id")
    private Long companyAddressId;

    @ApiModelProperty("确认退款金额")
    private BigDecimal returnAmount;

    @ApiModelProperty("处理备注")
    private String handleNote;

    @ApiModelProperty("处理人")
    private String handleMan;

    @ApiModelProperty("收货备注")
    private String receiveNote;

    @ApiModelProperty("收货人")
    private String receiveMan;

    @ApiModelProperty("申请状态：1->退货中；2->已完成；3->已拒绝")
    private Integer status;
}
