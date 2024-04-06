package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsBrand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author moli
 * @time 2024-04-05 21:03:49
 * @description 首页品牌信息
 */
@FeignClient(value = "mall-admin", contextId = "SmsHomeBrandService")
public interface SmsHomeBrandService {

    @GetMapping("/home/brand/app/list")
    CommonResult<CommonPage<PmsBrand>> appList(@RequestParam(value = "brandName", required = false) String brandName,
                                               @RequestParam(value = "showStatus", required = false) Integer showStatus,
                                               @RequestParam("pageNum") Integer pageNum,
                                               @RequestParam("pageSize") Integer pageSize);
}
