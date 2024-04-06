package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author moli
 * @time 2024-04-05 23:26:46
 * @description 首页新品管理
 */
@FeignClient(value = "mall-admin", contextId = "SmsHomeNewProductService")
public interface SmsHomeNewProductService {

    @GetMapping(value = "/home/newProduct/app/list")
    CommonResult<List<PmsProduct>> newProductsRecommend(@RequestParam(value = "productName", required = false) String productName,
                                                              @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                              @RequestParam("pageNum") Integer pageNum,
                                                              @RequestParam("pageSize") Integer pageSize);

    @GetMapping(value = "home/newProduct/hotList")
    CommonResult<List<PmsProduct>> hotList(@RequestParam(value = "productName", required = false) String productName,
                                           @RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize);
}
