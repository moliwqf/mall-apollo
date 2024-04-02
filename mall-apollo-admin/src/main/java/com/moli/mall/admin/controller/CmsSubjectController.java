package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.CmsSubjectService;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.CmsSubject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 16:41:41
 * @description 商品专题管理
 */
@RestController
@Api(tags = "CmsSubjectController", value = "商品专题管理")
@RequestMapping("/subject")
public class CmsSubjectController {

    @Resource
    private CmsSubjectService cmsSubjectService;

    @ApiOperation("获取全部商品专题")
    @GetMapping("/listAll")
    public CommonResult<List<CmsSubject>> listAll() {
        List<CmsSubject> subjectList = cmsSubjectService.listAll();
        return CommonResult.success(subjectList);
    }
}
