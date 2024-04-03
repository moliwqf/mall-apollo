package com.moli.mall.admin.controller;

import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.admin.dto.PmsBrandParams;
import com.moli.mall.admin.service.PmsBrandService;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:32:56
 * @description 品牌模块
 */
@Api(tags = "PmsBrandController", value = "品牌模块")
@RestController
@RequestMapping("/brand")
public class PmsBrandController {

    @Resource
    private PmsBrandService pmsBrandService;

    @ApiOperation(value = "批量更新厂家制造商状态")
    @PostMapping("/update/factoryStatus")
    public CommonResult<?> updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                               @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = pmsBrandService.updateFactoryStatus(ids, factoryStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation(value = "批量更新显示状态")
    @PostMapping("/update/showStatus")
    public CommonResult<?> updateShowStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("showStatus") Integer showStatus) {
        int count = pmsBrandService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation(value = "添加品牌")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<?> create(@Validated @RequestBody PmsBrandParams pmsBrand) {
        int count = pmsBrandService.createBrand(pmsBrand);
        if (count == 1) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation(value = "更新品牌")
    @PostMapping("/update/{id}")
    public CommonResult<?> update(@PathVariable("id") Long id,
                                  @Validated @RequestBody PmsBrandParams pmsBrandParam) {
        int count = pmsBrandService.updateBrand(id, pmsBrandParam);
        if (count == 1) {
            return CommonResult.success(count);
        }
        return CommonResult.fail();
    }

    @ApiOperation(value = "删除品牌")
    @GetMapping("/delete/{id}")
    public CommonResult<?> delete(@PathVariable("id") Long id) {
        int count = pmsBrandService.deleteBrand(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.fail();
        }
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @GetMapping("/{id}")
    public CommonResult<PmsBrand> info(@PathVariable("id") Long id) {
        return CommonResult.success(pmsBrandService.info(id));
    }

    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<PmsBrand>> getList(@RequestParam(value = "keyword", required = false) String keyword) {
        List<PmsBrand> brandList = pmsBrandService.listBrand(keyword, PageParamsContextHolder.getPageNum(), PageParamsContextHolder.getPageSize());
        return CommonResult.success(CommonPage.restPage(brandList));
    }
}
