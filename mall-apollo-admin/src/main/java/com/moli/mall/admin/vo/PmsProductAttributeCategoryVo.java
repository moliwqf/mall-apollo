package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.PmsProductAttribute;
import com.moli.mall.mbg.model.PmsProductAttributeCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-03 10:51:32
 * @description 包含有分类下属性的vo
 */
public class PmsProductAttributeCategoryVo extends PmsProductAttributeCategory {

    @Getter
    @Setter
    @ApiModelProperty(value = "商品属性列表")
    private List<PmsProductAttribute> productAttributeList;
}
