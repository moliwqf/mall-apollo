package com.moli.mall.admin.service;


import com.moli.mall.mbg.model.UmsResource;

import java.util.List;
import java.util.Map;

/**
 * @author moli
 * @time 2024-04-01 15:03:11
 * @description 资源服务层
 */
public interface UmsResourceService {
    /**
     * 查询所有后台资源
     */
    List<UmsResource> listAll();

    /**
     * 分页模糊查询
     */
    List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageNum, Integer pageSize);

    /**
     * 创建资源
     */
    int create(UmsResource umsResource);

    /**
     * 更新资源信息
     */
    int update(Long id, UmsResource umsResource);

    /**
     * 获取资源信息
     */
    UmsResource info(Long id);

    /**
     * 删除资源
     */
    int delete(Long id);

    /**
     * 初始化资源角色关联数据
     * @return 资源对应的角色信息
     */
    Map<String, List<String>> initResourceRolesMap();
}
