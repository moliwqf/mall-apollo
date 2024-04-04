package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.OmsCompanyAddressService;
import com.moli.mall.mbg.mapper.OmsCompanyAddressMapper;
import com.moli.mall.mbg.model.OmsCompanyAddress;
import com.moli.mall.mbg.model.OmsCompanyAddressExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 17:10:45
 * @description 收货地址管理
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {

    @Resource
    private OmsCompanyAddressMapper omsCompanyAddressMapper;

    @Override
    public List<OmsCompanyAddress> list() {
        return omsCompanyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
