package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.CmsSubject;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-02 16:42:23
 * @description 商品专题服务层
 */
public interface CmsSubjectService {
    /**
     * 获取全部商品专题
     */
    List<CmsSubject> listAll();
}
