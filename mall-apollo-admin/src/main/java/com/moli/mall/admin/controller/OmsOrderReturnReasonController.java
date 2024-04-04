package com.moli.mall.admin.controller;

import com.moli.mall.admin.annotation.FlagValidator;
import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.service.OmsOrderReturnReasonService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsOrderReturnReason;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 16:44:24
 * @description 退货原因管理
 */
@RestController
@Api(tags = "OmsOrderReturnReasonController", value = "退货原因管理")
@RequestMapping("/returnReason")
public class OmsOrderReturnReasonController {

    @Resource
    private OmsOrderReturnReasonService omsOrderReturnReasonService;

    @ApiOperation("修改退货原因启用状态")
    @PostMapping("/update/status")
    public CommonResult<?> updateStatus(
            @FlagValidator(value = {"0", "1"}, message = "只允许状态为0或1")
            @RequestParam(value = "status") Integer status,
            @RequestParam("ids") List<Long> ids) {
        int count = omsOrderReturnReasonService.updateStatus(ids, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取单个退货原因详情信息")
    @GetMapping("/{id}")
    public CommonResult<OmsOrderReturnReason> getItem(@PathVariable Long id) {
        OmsOrderReturnReason reason = omsOrderReturnReasonService.getItem(id);
        return CommonResult.success(reason);
    }

    @ApiOperation("添加退货原因")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody OmsOrderReturnReason returnReason) {
        int count = omsOrderReturnReasonService.create(returnReason);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("修改退货原因")
    @PostMapping("/update/{id}")
    public CommonResult<?> update(@PathVariable Long id,
                                  @RequestBody OmsOrderReturnReason returnReason) {
        int count = omsOrderReturnReasonService.update(id, returnReason);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("批量删除退货原因")
    @PostMapping("/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = omsOrderReturnReasonService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("分页查询全部退货原因")
    @GetMapping("/list")
    public CommonResult<CommonPage<OmsOrderReturnReason>> list() {
        List<OmsOrderReturnReason> reasonList = omsOrderReturnReasonService.list(PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(reasonList));
    }
}
