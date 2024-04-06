package com.moli.mall.portal.service.impl;

import com.moli.mall.mbg.mapper.OmsCartItemMapper;
import com.moli.mall.mbg.model.OmsCartItem;
import com.moli.mall.mbg.model.OmsCartItemExample;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.service.OmsCartItemService;
import com.moli.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 23:04:30
 * @description 购物车管理
 */
@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {

    @Resource
    private OmsCartItemMapper omsCartItemMapper;

    @Resource
    private UmsMemberService umsMemberService;

    @Override
    public List<OmsCartItem> list() {
        UmsMember current = umsMemberService.info();
        OmsCartItemExample omsCartItemExample = new OmsCartItemExample();
        omsCartItemExample.createCriteria().andMemberIdEqualTo(current.getId()).andDeleteStatusEqualTo(0);
        return omsCartItemMapper.selectByExample(omsCartItemExample);
    }
}
