package com.moli.mall.admin.dto;

import com.moli.mall.admin.annotation.FlagValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-03 15:52:35
 * @description 商品分类参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsProductCategoryParams {

    @ApiModelProperty("父分类的编号")
    private Long parentId;

    @NotEmpty
    @ApiModelProperty(value = "商品分类名称", required = true)
    private String name;

    @ApiModelProperty("分类单位")
    private String productUnit;

    @FlagValidator(value = {"0", "1"}, message = "状态只能为0或1")
    @ApiModelProperty("是否在导航栏显示")
    private Integer navStatus;

    @FlagValidator(value = {"0", "1"}, message = "状态只能为0或1")
    @ApiModelProperty("是否进行显示")
    private Integer showStatus;

    @Min(value = 0)
    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("产品相关筛选属性集合")
    private List<Long> productAttributeIdList;
}
