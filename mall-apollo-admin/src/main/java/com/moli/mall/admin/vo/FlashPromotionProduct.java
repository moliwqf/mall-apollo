package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.PmsProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author moli
 * @time 2024-04-05 22:38:55
 * @description 秒杀信息和商品对象封装
 */
@Getter
@Setter
public class FlashPromotionProduct extends PmsProduct {

    @ApiModelProperty("秒杀价格")
    private BigDecimal flashPromotionPrice;

    @ApiModelProperty("秒杀数量")
    private Integer flashPromotionCount;

    @ApiModelProperty("秒杀限制")
    private Integer flashPromotionLimit;
}
