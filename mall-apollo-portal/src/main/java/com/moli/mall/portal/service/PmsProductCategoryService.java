package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.PmsProductCategory;
import com.moli.mall.portal.vo.PmsProductCategoryWithChildrenVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 13:40:19
 * @description 产品分类模块
 */
@FeignClient(value = "mall-admin", contextId = "PmsProductCategoryService")
public interface PmsProductCategoryService {

    @GetMapping("/productCategory/all/{parentId}")
    CommonResult<List<PmsProductCategory>> getListByParentId(@PathVariable Long parentId);

    @GetMapping("/productCategory/list/withChildren")
    CommonResult<List<PmsProductCategoryWithChildrenVo>> categoryTreeList();
}
