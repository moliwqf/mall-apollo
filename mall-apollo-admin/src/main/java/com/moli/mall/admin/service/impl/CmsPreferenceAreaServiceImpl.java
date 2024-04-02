package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.CmsPreferenceAreaService;
import com.moli.mall.mbg.mapper.CmsPrefrenceAreaMapper;
import com.moli.mall.mbg.model.CmsPrefrenceArea;
import com.moli.mall.mbg.model.CmsPrefrenceAreaExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 16:01:06
 * @description 商品优选服务实现层
 */
@Service
public class CmsPreferenceAreaServiceImpl implements CmsPreferenceAreaService {

    @Resource
    private CmsPrefrenceAreaMapper cmsPrefrenceAreaMapper;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return cmsPrefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
