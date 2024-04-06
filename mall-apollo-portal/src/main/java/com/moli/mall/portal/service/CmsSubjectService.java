package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.CmsSubject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author moli
 * @time 2024-04-06 13:50:17
 * @description 商品专题管理
 */
@FeignClient(value = "mall-admin", contextId = "CmsSubjectService")
public interface CmsSubjectService {

    @RequestMapping(value = "/subject/subjectList", method = RequestMethod.GET)
    CommonResult<CommonPage<CmsSubject>> getSubjectList(@RequestParam(required = false) Long cateId,
                                                        @RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize);
}
