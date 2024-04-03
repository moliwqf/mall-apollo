package com.moli.mall.admin.controller;

import cn.hutool.core.io.FileUtil;
import com.moli.mall.admin.context.UploadStrategyContext;
import com.moli.mall.admin.vo.UploadedEntity;
import com.moli.mall.common.domain.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-03 12:16:22
 * @description MinIO对象存储管理
 */
@Api(tags = "MinioController", value = "MinIO对象存储管理")
@RestController
@RequestMapping("/minio")
public class MinioController {

    @Resource
    private UploadStrategyContext uploadStrategyContext;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public CommonResult<UploadedEntity> upload(@RequestPart("file") MultipartFile file) {
        UploadedEntity uploadedEntity = uploadStrategyContext.executeUploadStrategy(file, "mall/");
        return CommonResult.success(uploadedEntity);
    }
}
