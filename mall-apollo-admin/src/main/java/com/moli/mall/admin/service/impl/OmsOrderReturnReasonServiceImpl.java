package com.moli.mall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.OmsOrderReturnReasonService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.OmsOrderReturnReasonMapper;
import com.moli.mall.mbg.model.OmsOrder;
import com.moli.mall.mbg.model.OmsOrderReturnReason;
import com.moli.mall.mbg.model.OmsOrderReturnReasonExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-04 20:10:00
 * @description 退货原因管理
 */
@Service
public class OmsOrderReturnReasonServiceImpl implements OmsOrderReturnReasonService {

    @Resource
    private OmsOrderReturnReasonMapper omsOrderReturnReasonMapper;

    @Override
    @Transactional
    public int create(OmsOrderReturnReason returnReason) {
        return omsOrderReturnReasonMapper.insert(returnReason);
    }

    @Override
    @Transactional
    public int update(Long id, OmsOrderReturnReason returnReason) {
        OmsOrderReturnReason raw = omsOrderReturnReasonMapper.selectByPrimaryKey(id);
        if (Objects.isNull(raw)) {
            AssetsUtil.fail("不存在该内容");
        }
        returnReason.setId(id);
        return omsOrderReturnReasonMapper.updateByPrimaryKeySelective(returnReason);
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        OmsOrderReturnReasonExample omsOrderReturnReasonExample = new OmsOrderReturnReasonExample();
        omsOrderReturnReasonExample.createCriteria().andIdIn(ids);
        return omsOrderReturnReasonMapper.deleteByExample(omsOrderReturnReasonExample);
    }

    @Override
    public List<OmsOrderReturnReason> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OmsOrderReturnReasonExample omsOrderReturnReasonExample = new OmsOrderReturnReasonExample();
        omsOrderReturnReasonExample.setOrderByClause("sort desc");
        return omsOrderReturnReasonMapper.selectByExample(omsOrderReturnReasonExample);
    }

    @Override
    public OmsOrderReturnReason getItem(Long id) {
        return omsOrderReturnReasonMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int updateStatus(List<Long> ids, Integer status) {

        OmsOrderReturnReason update = new OmsOrderReturnReason();
        update.setStatus(status);
        OmsOrderReturnReasonExample omsOrderReturnReasonExample = new OmsOrderReturnReasonExample();
        omsOrderReturnReasonExample.createCriteria().andIdIn(ids);
        return omsOrderReturnReasonMapper.updateByExampleSelective(update, omsOrderReturnReasonExample);
    }
}
