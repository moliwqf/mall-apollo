package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProduct;
import com.moli.mall.portal.vo.PmsPortalProductDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author moli
 * @time 2024-04-06 10:14:53
 * @description 商品服务
 */
@FeignClient(value = "mall-admin", contextId = "PmsProductService")
public interface PmsProductService {

    @GetMapping("/product/recommend/list")
    CommonResult<CommonPage<PmsProduct>> recommendProductList(@RequestParam("pageNum") Integer pageNum,
                                                              @RequestParam("pageSize") Integer pageSize);

    @GetMapping("/product/search")
    CommonPage<PmsProduct> search(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "brandId", required = false) Long brandId,
                                  @RequestParam(value = "productCategoryId", required = false) Long productCategoryId,
                                  @RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam("sort") Integer sort);

    @GetMapping("/product/detail")
    CommonResult<PmsPortalProductDetailVo> detail(@RequestParam("productId") Long productId);
}
