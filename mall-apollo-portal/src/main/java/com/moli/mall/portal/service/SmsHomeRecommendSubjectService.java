package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.CmsSubject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 09:24:15
 * @description 首页专题推荐管理
 */
@FeignClient(value = "mall-admin", contextId = "SmsHomeRecommendSubjectService")
public interface SmsHomeRecommendSubjectService {

    @GetMapping("/home/recommendSubject/app/list")
    CommonResult<List<CmsSubject>> appList(@RequestParam(value = "subjectName", required = false) String subjectName,
                                           @RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize);
}
