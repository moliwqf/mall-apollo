package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.UmsMemberLevelService;
import com.moli.mall.mbg.mapper.UmsMemberLevelMapper;
import com.moli.mall.mbg.model.UmsMemberLevel;
import com.moli.mall.mbg.model.UmsMemberLevelExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 22:21:12
 * @description 用户成员登录服务实现层
 */
@Service
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {

    @Resource
    private UmsMemberLevelMapper umsMemberLevelMapper;

    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        UmsMemberLevelExample umsMemberLevelExample = new UmsMemberLevelExample();
        umsMemberLevelExample.createCriteria().andDefaultStatusEqualTo(defaultStatus);
        return umsMemberLevelMapper.selectByExample(umsMemberLevelExample);
    }
}
