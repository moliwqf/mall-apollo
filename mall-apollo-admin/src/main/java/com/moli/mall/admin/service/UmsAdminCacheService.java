package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.UmsAdmin;

/**
 * @author moli
 * @time 2024-03-30 22:11:46
 * @description 后台用户缓存服务层
 */
public interface UmsAdminCacheService {

    /**
     * 根据adminId 获取后台用户
     * @param adminId 用户id
     * @return UmsAdmin
     */
    UmsAdmin getAdmin(Long adminId);

    /**
     * 将登陆用户放入到redis中
     * @param adminId 用户id
     * @param admin 用户信息
     */
    void setAdmin(Long adminId, UmsAdmin admin);
}
