package com.moli.mall.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author moli
 * @time 2024-04-03 11:11:18
 * @description 商品分类对应属性信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductAttrInfoVo {

    @ApiModelProperty("商品属性ID")
    private Long attributeId;

    @ApiModelProperty("商品属性分类ID")
    private Long attributeCategoryId;
}
