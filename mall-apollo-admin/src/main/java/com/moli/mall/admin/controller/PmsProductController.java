package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.dto.PmsProductParams;
import com.moli.mall.admin.dto.PmsProductQueryParams;
import com.moli.mall.admin.service.PmsProductService;
import com.moli.mall.admin.vo.PmsProductResultVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:37:53
 * @description 产品模块
 */
@Api(tags = "PmsProductController", value = "产品模块")
@RestController
@RequestMapping("/product")
public class PmsProductController {

    @Resource
    private PmsProductService pmsProductService;

    @ApiOperation("根据商品名称或货号模糊查询")
    @GetMapping("/simpleList")
    public CommonResult<List<PmsProduct>> getList(String keyword) {
        List<PmsProduct> productList = pmsProductService.list(keyword);
        return CommonResult.success(productList);
    }

    @ApiOperation("批量修改审核状态")
    @PostMapping("/update/verifyStatus")
    public CommonResult<?> updateVerifyStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("verifyStatus") Integer verifyStatus,
                                              @RequestParam("detail") String detail) {
        int count = pmsProductService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("批量上下架")
    @PostMapping("/update/publishStatus")
    public CommonResult<?> updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                               @RequestParam("publishStatus") Integer publishStatus) {
        int count = pmsProductService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("批量设为新品")
    @PostMapping("/update/newStatus")
    public CommonResult<?> updateNewStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("newStatus") Integer newStatus) {
        int count = pmsProductService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("批量推荐商品")
    @PostMapping("/update/recommendStatus")
    public CommonResult<?> updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                                 @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = pmsProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("批量修改删除状态")
    @PostMapping("/update/deleteStatus")
    public CommonResult<?> updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = pmsProductService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("创建商品")
    @PostMapping("/create")
    public CommonResult<?> create(@RequestBody PmsProductParams productParam) {
        int count = pmsProductService.create(productParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("更新商品")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<?> update(@PathVariable Long id, @RequestBody PmsProductParams productParam) {
        int count = pmsProductService.update(id, productParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("根据商品id获取商品编辑信息")
    @GetMapping("/updateInfo/{id}")
    @ResponseBody
    public CommonResult<PmsProductResultVo> getUpdateInfo(@PathVariable Long id) {
        PmsProductResultVo productResult = pmsProductService.getUpdateInfo(id);
        return CommonResult.success(productResult);
    }

    @ApiOperation("查询商品")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsProduct>> getList(PmsProductQueryParams productQueryParam) {
        List<PmsProduct> productList = pmsProductService.list(productQueryParam, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(productList));
    }
}
