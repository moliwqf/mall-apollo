package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.SmsHomeAdvertise;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-04 20:30:03
 * @description 首页轮播广告管理
 */
public interface SmsHomeAdvertiseService {
    /**
     * 分页查询
     */
    List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageNum, Integer pageSize);

    /**
     * 根据id查询
     */
    SmsHomeAdvertise getItem(Long id);

    /**
     * 更新广告
     */
    int update(Long id, SmsHomeAdvertise advertise);

    /**
     * 更新状态 - 上线/下线
     */
    int updateStatus(Long id, Integer status);

    /**
     * 批量删除
     */
    int delete(List<Long> ids);

    /**
     * 添加广告
     */
    int create(SmsHomeAdvertise advertise);
}
