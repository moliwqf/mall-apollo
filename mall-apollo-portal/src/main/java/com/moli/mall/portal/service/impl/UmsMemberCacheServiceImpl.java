package com.moli.mall.portal.service.impl;

import com.moli.mall.common.service.RedisService;
import com.moli.mall.mbg.model.UmsAdmin;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.service.UmsMemberCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-06 15:17:31
 * @description 会员用户缓存服务
 */
@Service
public class UmsMemberCacheServiceImpl implements UmsMemberCacheService {

    @Resource
    private RedisService redisService;

    @Value("${redis.database:mall}")
    private String REDIS_DATABASE;

    @Value("${redis.key.prefix.member:'ums:member'}")
    private String REDIS_ADMIN_PREFIX_KEY;

    @Value("${redis.expire-time:86400}")
    private int REDIS_EXPIRE_TIME;

    @Override
    public UmsMember getMember(Long memberId) {
        String key = REDIS_DATABASE + ":" + REDIS_ADMIN_PREFIX_KEY + ":" + memberId;
        return (UmsMember) redisService.get(key);
    }

    @Override
    public void setMember(Long memberId, UmsMember member) {
        String key = REDIS_DATABASE + ":" + REDIS_ADMIN_PREFIX_KEY + ":" + memberId;
        redisService.set(key, member, REDIS_EXPIRE_TIME);
    }

    @Override
    public void delMember(Long memberId) {
        String key = REDIS_DATABASE + ":" + REDIS_ADMIN_PREFIX_KEY + ":" + memberId;
        redisService.del(key);
    }
}
