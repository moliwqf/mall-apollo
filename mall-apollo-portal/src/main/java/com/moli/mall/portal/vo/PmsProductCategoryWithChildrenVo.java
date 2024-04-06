package com.moli.mall.portal.vo;

import com.moli.mall.mbg.model.PmsProductCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 23:22:37
 * @description 商品分类，包含子分类
 */
@Getter
@Setter
public class PmsProductCategoryWithChildrenVo extends PmsProductCategory {

    @ApiModelProperty("子分类")
    private List<PmsProductCategoryWithChildrenVo> children;
}
