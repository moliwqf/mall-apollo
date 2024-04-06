package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.mbg.model.SmsFlashPromotionProductRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author moli
 * @time 2024-04-05 14:31:43
 * @description 限时购及商品信息封装
 */
public class SmsFlashPromotionProductVo extends SmsFlashPromotionProductRelation {

    @Getter
    @Setter
    @ApiModelProperty("关联商品")
    private PmsProduct product;
}
