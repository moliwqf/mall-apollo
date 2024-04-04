package com.moli.mall.admin.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.SmsHomeAdvertiseService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.SmsHomeAdvertiseMapper;
import com.moli.mall.mbg.model.SmsHomeAdvertise;
import com.moli.mall.mbg.model.SmsHomeAdvertiseExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 20:30:15
 * @description 首页轮播广告管理
 */
@Service
public class SmsHomeAdvertiseServiceImpl implements SmsHomeAdvertiseService {

    @Resource
    private SmsHomeAdvertiseMapper smsHomeAdvertiseMapper;

    @Override
    public List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        SmsHomeAdvertiseExample smsHomeAdvertiseExample = new SmsHomeAdvertiseExample();
        SmsHomeAdvertiseExample.Criteria criteria = smsHomeAdvertiseExample.createCriteria();

        if (StrUtil.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }

        if (Objects.nonNull(type)) {
            criteria.andTypeEqualTo(type);
        }

        if (StrUtil.isNotEmpty(endTime)) {
            DateTime day = DateUtil.parse(endTime, "yyyy-MM-dd");
            Date begin = DateUtil.beginOfDay(day);
            Date end = DateUtil.endOfDay(day);
            criteria.andEndTimeBetween(begin, end);
        }
        return smsHomeAdvertiseMapper.selectByExample(smsHomeAdvertiseExample);
    }

    @Override
    public SmsHomeAdvertise getItem(Long id) {
        return smsHomeAdvertiseMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int update(Long id, SmsHomeAdvertise advertise) {
        SmsHomeAdvertise raw = smsHomeAdvertiseMapper.selectByPrimaryKey(id);
        if (Objects.isNull(raw)) {
            AssetsUtil.fail("不存在该广告");
        }
        advertise.setId(id);
        return smsHomeAdvertiseMapper.updateByPrimaryKeySelective(advertise);
    }

    @Override
    @Transactional
    public int updateStatus(Long id, Integer status) {
        SmsHomeAdvertise raw = smsHomeAdvertiseMapper.selectByPrimaryKey(id);
        if (Objects.isNull(raw)) {
            AssetsUtil.fail("不存在该广告");
        }
        SmsHomeAdvertise advertise = new SmsHomeAdvertise();
        advertise.setId(id);
        advertise.setStatus(status);
        return smsHomeAdvertiseMapper.updateByPrimaryKeySelective(advertise);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        SmsHomeAdvertiseExample smsHomeAdvertiseExample = new SmsHomeAdvertiseExample();
        smsHomeAdvertiseExample.createCriteria().andIdIn(ids);
        return smsHomeAdvertiseMapper.deleteByExample(smsHomeAdvertiseExample);
    }

    @Override
    @Transactional
    public int create(SmsHomeAdvertise advertise) {
        advertise.setClickCount(0);
        advertise.setOrderCount(0);
        return smsHomeAdvertiseMapper.insert(advertise);
    }
}
