package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.OmsCompanyAddress;
import com.moli.mall.mbg.model.OmsOrderReturnApply;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author moli
 * @time 2024-04-04 17:03:14
 * @description 申请信息封装
 */
public class OmsOrderReturnApplyVo extends OmsOrderReturnApply {

    @Getter
    @Setter
    @ApiModelProperty(value = "公司收货地址")
    private OmsCompanyAddress companyAddress;
}
