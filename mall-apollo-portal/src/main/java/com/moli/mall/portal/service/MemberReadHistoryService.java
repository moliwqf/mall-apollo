package com.moli.mall.portal.service;

import com.moli.mall.portal.domain.MemberReadHistory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-06 17:23:14
 * @description 会员商品浏览记录管理
 */
public interface MemberReadHistoryService {
    /**
     * 创建浏览记录
     */
    int create(MemberReadHistory memberReadHistory);

    /**
     * 批量删除浏览记录
     */
    int delete(List<String> ids);

    /**
     * 清空聊天记录
     */
    void clear();

    /**
     * 分页查询浏览记录
     */
    Page<MemberReadHistory> list(Integer pageNum, Integer pageSize);
}
