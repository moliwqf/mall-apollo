package com.moli.mall.portal.service;

import com.moli.mall.portal.domain.MemberBrandAttention;
import org.springframework.data.domain.Page;

/**
 * @author moli
 * @time 2024-04-06 20:46:25
 * @description 会员关注品牌管理
 */
public interface MemberAttentionService {
    /**
     * 添加关注信息
     */
    int add(MemberBrandAttention memberBrandAttention);

    /**
     * 根据品牌id删除关注信息
     */
    int delete(Long brandId);

    /**
     * 分页获取关注信息
     */
    Page<MemberBrandAttention> list(Integer pageNum, Integer pageSize);

    /**
     * 根据品牌id获取关注信息
     */
    MemberBrandAttention detail(Long brandId);

    /**
     * 清空关注列表
     */
    void clear();
}
