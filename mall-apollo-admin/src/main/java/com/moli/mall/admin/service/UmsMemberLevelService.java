package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.UmsMemberLevel;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 22:20:59
 * @description 用户成员登录服务层
 */
public interface UmsMemberLevelService {
    /**
     * 根据默认登录查询所有的成员等级
     * @param defaultStatus 默认登录
     * @return UmsMemberLevel
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
