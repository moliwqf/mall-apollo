package com.moli.mall.admin.vo;

import com.moli.mall.mbg.model.UmsMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author moli
 * @time 2024-03-30 22:28:29
 * @description name + icon + menuList 视图
 */
@Data
@Builder
public class UmsAdminNameIconWithMenusAndRolesVo {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("头像")
    private String icon;

    @ApiModelProperty("菜单列表")
    private List<UmsMenu> menuList;

    @ApiModelProperty("角色列表")
    private List<String> roleList;
}
