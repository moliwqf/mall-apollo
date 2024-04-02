package com.moli.mall.admin.controller;

import com.moli.mall.admin.service.CmsPreferenceAreaService;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.CmsPrefrenceArea;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 15:59:46
 * @description 商品优选管理
 */
@RestController
@Api(tags = "CmsPreferenceAreaController", value = "商品优选管理")
@RequestMapping("/prefrenceArea")
public class CmsPreferenceAreaController {

    @Resource
    private CmsPreferenceAreaService cmsPreferenceAreaService;

    @ApiOperation("获取所有商品优选")
    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<CmsPrefrenceArea>> listAll() {
        List<CmsPrefrenceArea> prefrenceAreaList = cmsPreferenceAreaService.listAll();
        return CommonResult.success(prefrenceAreaList);
    }
}
