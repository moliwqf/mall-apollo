package com.moli.mall.admin.service.impl;

import com.moli.mall.admin.service.UmsAdminCacheService;
import com.moli.mall.common.service.RedisService;
import com.moli.mall.mbg.model.UmsAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-03-30 22:12:59
 * @description 缓存服务实现层
 */
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    @Resource
    private RedisService redisService;

    @Value("${redis.database:mall}")
    private String REDIS_DATABASE;

    @Value("${redis.key.prefix.admin:'ums:admin'}")
    private String REDIS_ADMIN_PREFIX_KEY;

    @Value("${redis.expire-time:86400}")
    private int REDIS_EXPIRE_TIME;

    @Override
    public UmsAdmin getAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_ADMIN_PREFIX_KEY + ":" + adminId;
        return (UmsAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(Long adminId, UmsAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_ADMIN_PREFIX_KEY + ":" + adminId;
        redisService.set(key, admin, REDIS_EXPIRE_TIME);
    }

    @Override
    public void delAdmin(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_ADMIN_PREFIX_KEY + ":" + adminId;
        redisService.del(key);
    }
}
