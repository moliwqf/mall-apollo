package com.moli.mall.portal.service;

import com.moli.mall.portal.domain.MemberProductCollection;
import org.springframework.data.domain.Page;

/**
 * @author moli
 * @time 2024-04-06 22:50:56
 * @description 会员收藏管理
 */
public interface MemberProductCollectionService {
    /**
     * 添加用户收藏信息
     */
    int add(MemberProductCollection productCollection);

    /**
     * 删除用户收藏信息
     */
    int delete(Long productId);

    /**
     * 分页查询用户收藏信息
     */
    Page<MemberProductCollection> list(Integer pageNum, Integer pageSize);

    /**
     * 根据商品id查询
     */
    MemberProductCollection detail(Long productId);

    /**
     * 清空用户收藏商品信息
     */
    void clear();
}
