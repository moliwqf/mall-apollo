package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.OmsOrderSettingService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.OmsOrderSettingMapper;
import com.moli.mall.mbg.model.OmsOrderSetting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 16:41:42
 * @description 订单设置管服务实现层
 */
@Service
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {

    @Resource
    private OmsOrderSettingMapper omsOrderSettingMapper;

    @Override
    public OmsOrderSetting info(Long id) {
        return omsOrderSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int update(Long id, OmsOrderSetting orderSetting) {
        OmsOrderSetting rawSetting = omsOrderSettingMapper.selectByPrimaryKey(id);
        if (Objects.isNull(rawSetting)) {
            AssetsUtil.fail("订单设置不存在!!");
        }
        orderSetting.setId(id);
        return omsOrderSettingMapper.updateByPrimaryKeySelective(orderSetting);
    }
}
