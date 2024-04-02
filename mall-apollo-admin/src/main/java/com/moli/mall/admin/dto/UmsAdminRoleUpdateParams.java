package com.moli.mall.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-01 10:35:53
 * @description 后台用户角色更新参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UmsAdminRoleUpdateParams {

    @NotNull
    @ApiModelProperty("用户id")
    private Long adminId;
    @ApiModelProperty("角色id列表")
    private List<Long> roleIds;
}
