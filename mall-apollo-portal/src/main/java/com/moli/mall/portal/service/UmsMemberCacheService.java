package com.moli.mall.portal.service;

import com.moli.mall.mbg.model.UmsMember;

/**
 * @author moli
 * @time 2024-04-06 15:17:10
 * @description 会员用户缓存服务
 */
public interface UmsMemberCacheService {

    /**
     * 获取会员用户
     */
    UmsMember getMember(Long memberId);

    /**
     * 设置用户
     */
    void setMember(Long memberId, UmsMember member);

    /**
     * 删除用户
     */
    void delMember(Long memberId);
}
