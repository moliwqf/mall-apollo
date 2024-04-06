package com.moli.mall.portal.service.impl;

import com.moli.mall.mbg.model.SmsCoupon;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.dao.SmsCouponHistoryDao;
import com.moli.mall.portal.service.UmsMemberCouponService;
import com.moli.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 15:45:57
 * @description 用户优惠券管理
 */
@Service
public class UmsMemberCouponServiceImpl implements UmsMemberCouponService {

    @Resource
    private UmsMemberService umsMemberService;

    @Resource
    private SmsCouponHistoryDao smsCouponHistoryDao;

    @Override
    public List<SmsCoupon> list(Integer useStatus) {
        // 获取当前用户信息
        UmsMember currentUser = umsMemberService.info();
        return smsCouponHistoryDao.listHistories(currentUser.getId(), useStatus);
    }
}
