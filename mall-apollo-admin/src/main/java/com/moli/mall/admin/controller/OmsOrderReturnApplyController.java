package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.dto.OmsReturnApplyQueryParams;
import com.moli.mall.admin.dto.OmsUpdateStatusParams;
import com.moli.mall.admin.service.OmsOrderReturnApplyService;
import com.moli.mall.admin.vo.OmsOrderReturnApplyVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsOrderReturnApply;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 16:44:50
 * @description 订单退货申请管理
 */
@RestController
@Api(tags = "OmsOrderReturnApplyController", value = "订单退货申请管理")
@RequestMapping("/returnApply")
public class OmsOrderReturnApplyController {

    @Resource
    private OmsOrderReturnApplyService omsOrderReturnApplyService;

    @ApiOperation("修改申请状态")
    @PostMapping("/update/status/{id}")
    public CommonResult<?> updateStatus(@PathVariable Long id,
                                        @RequestBody OmsUpdateStatusParams statusParam) {
        int count = omsOrderReturnApplyService.updateStatus(id, statusParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("获取退货申请详情")
    @GetMapping("/{id}")
    public CommonResult<?> info(@PathVariable Long id) {
        OmsOrderReturnApplyVo result = omsOrderReturnApplyService.info(id);
        return CommonResult.success(result);
    }

    @ApiOperation("批量删除申请")
    @PostMapping("/delete")
    public CommonResult<?> delete(@RequestParam("ids") List<Long> ids) {
        int count = omsOrderReturnApplyService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation("分页查询退货申请")
    @GetMapping("/list")
    public CommonResult<CommonPage<OmsOrderReturnApply>> list(OmsReturnApplyQueryParams queryParam) {
        List<OmsOrderReturnApply> returnApplyList = omsOrderReturnApplyService.list(queryParam, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(returnApplyList));
    }
}
