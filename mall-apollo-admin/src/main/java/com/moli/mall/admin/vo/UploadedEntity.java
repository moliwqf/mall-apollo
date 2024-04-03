package com.moli.mall.admin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author moli
 * @time 2024-04-03 12:20:37
 * @description 已上传文件实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadedEntity {

    @ApiModelProperty("文件访问URL")
    private String url;

    @ApiModelProperty("文件名称")
    private String name;
}
