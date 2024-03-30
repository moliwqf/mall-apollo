package com.moli.mall.auth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author moli
 * @time 2024-03-30 15:03:28
 * @description Oath2 token封装信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class OAuth2TokenDto {

    @ApiModelProperty("访问令牌")
    private String token;

    @ApiModelProperty("刷令牌")
    private String refreshToken;

    @ApiModelProperty("访问令牌头前缀")
    private String tokenHead;

    @ApiModelProperty("有效时间（秒）")
    private int expiresIn;
}
