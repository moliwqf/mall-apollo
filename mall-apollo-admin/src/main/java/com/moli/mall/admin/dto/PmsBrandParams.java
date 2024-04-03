package com.moli.mall.admin.dto;

import com.moli.mall.admin.annotation.FlagValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author moli
 * @time 2024-04-03 23:48:37
 * @description 品牌传递参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsBrandParams {

    @NotEmpty
    @ApiModelProperty(value = "品牌名称", required = true)
    private String name;

    @ApiModelProperty(value = "品牌首字母")
    private String firstLetter;

    @Min(value = 0)
    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @FlagValidator(value = {"0", "1"}, message = "厂家状态不正确")
    @ApiModelProperty(value = "是否为厂家制造商")
    private Integer factoryStatus;

    @FlagValidator(value = {"0", "1"}, message = "显示状态不正确")
    @ApiModelProperty(value = "是否进行显示")
    private Integer showStatus;

    @NotEmpty
    @ApiModelProperty(value = "品牌logo", required = true)
    private String logo;

    @ApiModelProperty(value = "品牌大图")
    private String bigPic;

    @ApiModelProperty(value = "品牌故事")
    private String brandStory;

}
