package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.UmsMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * @author moli
 * @time 2024-04-01 14:21:42
 * @description 菜单分级视图
 */
@Getter
@Setter
public class UmsMenuNodeVo extends UmsMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNodeVo> children;
}
