package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.PmsSkuStockService;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsSkuStock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-03 10:27:05
 * @description sku商品库存管理
 */
@RestController
@Api(tags = "PmsSkuStockController", value = "sku商品库存管理")
@RequestMapping("/sku")
public class PmsSkuStockController {

    @Resource
    private PmsSkuStockService pmsSkuStockService;

    @ApiOperation("批量更新库存信息")
    @PostMapping("/update/{pid}")
    public CommonResult<?> update(@PathVariable Long pid, @RequestBody List<PmsSkuStock> skuStockList) {
        int count = pmsSkuStockService.update(pid, skuStockList);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation("根据商品编号及编号模糊搜索sku库存")
    @GetMapping(value = "/{pid}")
    public CommonResult<List<PmsSkuStock>> getList(@PathVariable Long pid, @RequestParam(value = "keyword", required = false) String keyword) {
        List<PmsSkuStock> skuStockList = pmsSkuStockService.getList(pid, keyword);
        return CommonResult.success(skuStockList);
    }
}
