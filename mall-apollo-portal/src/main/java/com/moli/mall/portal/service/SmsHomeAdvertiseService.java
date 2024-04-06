package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.SmsHomeAdvertise;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-05 20:24:41
 * @description 首页轮播广告管理
 */
@FeignClient(value = "mall-admin", contextId = "SmsHomeAdvertiseService")
public interface SmsHomeAdvertiseService {

    @GetMapping("/home/advertise/app/all")
    List<SmsHomeAdvertise> appList();
}
