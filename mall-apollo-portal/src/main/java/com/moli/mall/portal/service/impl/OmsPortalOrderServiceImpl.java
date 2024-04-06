package com.moli.mall.portal.service.impl;

import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.dto.OmsOrderQueryParams;
import com.moli.mall.portal.service.OmsOrderService;
import com.moli.mall.portal.service.OmsPortalOrderService;
import com.moli.mall.portal.service.UmsMemberService;
import com.moli.mall.portal.vo.OmsOrderDetailVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-06 16:45:03
 * @description 订单管理
 */
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    @Resource
    private OmsOrderService omsOrderService;

    @Resource
    private UmsMemberService umsMemberService;

    @Override
    public CommonPage<OmsOrderDetailVo> list(Integer status, Integer pageNum, Integer pageSize) {
        // 设置请求参数
        UmsMember current = umsMemberService.info();
        OmsOrderQueryParams queryParams = new OmsOrderQueryParams();
        queryParams.setStatus(status);
        queryParams.setMemberId(current.getId());
        // rpc调用
        CommonResult<CommonPage<OmsOrderDetailVo>> page = omsOrderService.detailList(queryParams, pageNum, pageSize);
        return page.getData();
    }
}
