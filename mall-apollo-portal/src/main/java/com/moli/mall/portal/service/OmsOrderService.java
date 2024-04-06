package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.portal.dto.OmsOrderQueryParams;
import com.moli.mall.portal.vo.OmsOrderDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author moli
 * @time 2024-04-06 16:49:14
 * @description 订单管理
 */
@FeignClient(value = "mall-admin", contextId = "OmsOrderService")
public interface OmsOrderService {

    @PostMapping("/order/detail/list")
    CommonResult<CommonPage<OmsOrderDetailVo>> detailList(@RequestBody OmsOrderQueryParams queryParam,
                                                          @RequestParam("pageNum") Integer pageNum,
                                                          @RequestParam("pageSize") Integer pageSize);
}
