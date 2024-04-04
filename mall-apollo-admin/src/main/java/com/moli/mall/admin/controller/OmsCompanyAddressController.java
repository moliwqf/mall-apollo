package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.OmsCompanyAddressService;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.OmsCompanyAddress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 17:10:15
 * @description 收货地址管理
 */
@RestController
@Api(tags = "OmsCompanyAddressController", value = "收货地址管理")
@RequestMapping("/companyAddress")
public class OmsCompanyAddressController {

    @Resource
    private OmsCompanyAddressService omsCompanyAddressService;

    @ApiOperation("获取所有收货地址")
    @GetMapping("/list")
    public CommonResult<List<OmsCompanyAddress>> list() {
        List<OmsCompanyAddress> companyAddressList = omsCompanyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
