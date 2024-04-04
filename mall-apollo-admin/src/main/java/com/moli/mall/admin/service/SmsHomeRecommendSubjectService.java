package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.SmsHomeRecommendSubject;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 22:13:19
 * @description 首页专题推荐管理
 */
public interface SmsHomeRecommendSubjectService {
    /**
     * 分页查询
     */
    List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    /**
     * 更新推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 更新排序
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量添加
     */
    int create(List<SmsHomeRecommendSubject> homeBrandList);
}
