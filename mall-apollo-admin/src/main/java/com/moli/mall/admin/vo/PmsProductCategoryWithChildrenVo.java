package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.PmsProductCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:17:02
 * @description 产品分类及其子分类
 */
public class PmsProductCategoryWithChildrenVo extends PmsProductCategory {
    @Getter
    @Setter
    @ApiModelProperty("子级分类")
    private List<PmsProductCategoryWithChildrenVo> children;
}
