package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.UmsMemberLevelService;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.UmsMemberLevel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 22:20:37
 * @description 会员等级管理
 */
@RestController
@Api(tags = "UmsMemberLevelController", value = "会员等级管理")
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {

    @Resource
    private UmsMemberLevelService umsMemberLevelService;

    @GetMapping("/list")
    @ApiOperation("查询所有会员等级")
    public CommonResult<List<UmsMemberLevel>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        List<UmsMemberLevel> memberLevelList = umsMemberLevelService.list(defaultStatus);
        return CommonResult.success(memberLevelList);
    }
}
