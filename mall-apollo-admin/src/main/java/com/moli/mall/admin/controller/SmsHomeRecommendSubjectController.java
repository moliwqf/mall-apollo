package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.SmsHomeRecommendSubjectService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.CmsSubject;
import com.moli.mall.mbg.model.SmsHomeRecommendSubject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 22:12:46
 * @description 首页专题推荐管理
 */
@RestController
@Api(tags = "SmsHomeRecommendSubjectController", value = "首页专题推荐管理")
@RequestMapping("/home/recommendSubject")
public class SmsHomeRecommendSubjectController {

    @Resource
    private SmsHomeRecommendSubjectService smsHomeRecommendSubjectService;

    @ApiOperation("添加首页推荐专题")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody List<SmsHomeRecommendSubject> homeBrandList) {
        int count = smsHomeRecommendSubjectService.create(homeBrandList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public CommonResult<?> updateSort(@PathVariable Long id, Integer sort) {
        int count = smsHomeRecommendSubjectService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping("/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = smsHomeRecommendSubjectService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public CommonResult<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                                 @RequestParam Integer recommendStatus) {
        int count = smsHomeRecommendSubjectService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("分页查询推荐")
    @GetMapping("/list")
    public CommonResult<CommonPage<SmsHomeRecommendSubject>> list(@RequestParam(value = "subjectName", required = false) String subjectName,
                                                                  @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus) {
        List<SmsHomeRecommendSubject> homeRecommendSubjects = smsHomeRecommendSubjectService.list(subjectName, recommendStatus, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(homeRecommendSubjects));
    }

    @ApiOperation("分页查询推荐专题")
    @GetMapping("/app/list")
    public CommonResult<List<CmsSubject>> appList(@RequestParam(value = "subjectName", required = false) String subjectName) {
        List<CmsSubject> homeRecommendSubjects = smsHomeRecommendSubjectService.appList(subjectName, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(homeRecommendSubjects);
    }
}
