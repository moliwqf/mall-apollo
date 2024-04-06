package com.moli.mall.portal.service.impl;

import com.moli.mall.mbg.mapper.UmsMemberReceiveAddressMapper;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.mbg.model.UmsMemberReceiveAddress;
import com.moli.mall.mbg.model.UmsMemberReceiveAddressExample;
import com.moli.mall.portal.service.UmsMemberReceiveAddressService;
import com.moli.mall.portal.service.UmsMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 16:28:00
 * @description 会员收货地址管理
 */
@Service
public class UmsMemberReceiveAddressServiceImpl implements UmsMemberReceiveAddressService {

    @Resource
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Resource
    private UmsMemberService umsMemberService;

    @Override
    @Transactional
    public int add(UmsMemberReceiveAddress address) {
        UmsMember current = umsMemberService.info();
        address.setMemberId(current.getId());
        return umsMemberReceiveAddressMapper.insert(address);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        UmsMember current = umsMemberService.info();
        UmsMemberReceiveAddressExample umsMemberReceiveAddressExample = new UmsMemberReceiveAddressExample();
        umsMemberReceiveAddressExample.createCriteria().andMemberIdEqualTo(current.getId()).andIdEqualTo(id);
        return umsMemberReceiveAddressMapper.deleteByExample(umsMemberReceiveAddressExample);
    }

    @Override
    @Transactional
    public int update(Long id, UmsMemberReceiveAddress address) {
        UmsMember current = umsMemberService.info();
        // 如果当前地址为默认地址，则修改之前的默认地址
        if (address.getDefaultStatus() == 1) {
            UmsMemberReceiveAddress defaultAddress = new UmsMemberReceiveAddress();
            defaultAddress.setDefaultStatus(0);
            UmsMemberReceiveAddressExample defaultExample = new UmsMemberReceiveAddressExample();
            defaultExample.createCriteria().andMemberIdEqualTo(current.getId()).andDefaultStatusEqualTo(1);
            umsMemberReceiveAddressMapper.updateByExampleSelective(defaultAddress, defaultExample);
        }
        UmsMemberReceiveAddressExample updateExample = new UmsMemberReceiveAddressExample();
        updateExample.createCriteria().andIdEqualTo(id).andMemberIdEqualTo(current.getId());
        return umsMemberReceiveAddressMapper.updateByExampleSelective(address, updateExample);
    }

    @Override
    public List<UmsMemberReceiveAddress> list() {
        UmsMember current = umsMemberService.info();
        UmsMemberReceiveAddressExample umsMemberReceiveAddressExample = new UmsMemberReceiveAddressExample();
        umsMemberReceiveAddressExample.createCriteria().andMemberIdEqualTo(current.getId());
        return umsMemberReceiveAddressMapper.selectByExample(umsMemberReceiveAddressExample);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id) {
        UmsMember current = umsMemberService.info();
        UmsMemberReceiveAddressExample umsMemberReceiveAddressExample = new UmsMemberReceiveAddressExample();
        umsMemberReceiveAddressExample.createCriteria().andMemberIdEqualTo(current.getId()).andIdEqualTo(id);
        List<UmsMemberReceiveAddress> address = umsMemberReceiveAddressMapper.selectByExample(umsMemberReceiveAddressExample);
        if (CollectionUtils.isEmpty(address)) return null;
        return address.get(0);
    }
}
