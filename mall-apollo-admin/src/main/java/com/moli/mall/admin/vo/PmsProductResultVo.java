package com.moli.mall.admin.vo;

import com.moli.mall.admin.dto.PmsProductParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author moli
 * @time 2024-04-02 16:03:20
 * @description 查询单个产品进行修改时返回的结果
 */
public class PmsProductResultVo extends PmsProductParams {
    @Getter
    @Setter
    @ApiModelProperty("商品所选分类的父id")
    private Long cateParentId;
}
