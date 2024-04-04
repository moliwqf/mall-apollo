package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.SmsCouponHistoryService;
import com.moli.mall.mbg.mapper.SmsCouponHistoryMapper;
import com.moli.mall.mbg.model.SmsCouponHistory;
import com.moli.mall.mbg.model.SmsCouponHistoryExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-05 00:42:39
 * @description 优惠券领取记录管理
 */
@Service
public class SmsCouponHistoryServiceImpl implements SmsCouponHistoryService {

    @Resource
    private SmsCouponHistoryMapper smsCouponHistoryMapper;

    @Override
    public List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsCouponHistoryExample smsCouponHistoryExample = new SmsCouponHistoryExample();
        smsCouponHistoryExample.setOrderByClause("create_time desc, use_time desc");
        SmsCouponHistoryExample.Criteria criteria = smsCouponHistoryExample.createCriteria();

        if (Objects.nonNull(couponId)) {
            criteria.andCouponIdEqualTo(couponId);
        }
        if (Objects.nonNull(useStatus)) {
            criteria.andUseStatusEqualTo(useStatus);
        }
        if (StrUtil.isNotEmpty(orderSn)) {
            criteria.andOrderSnLike("%" + orderSn + "%");
        }

        return smsCouponHistoryMapper.selectByExample(smsCouponHistoryExample);
    }
}
