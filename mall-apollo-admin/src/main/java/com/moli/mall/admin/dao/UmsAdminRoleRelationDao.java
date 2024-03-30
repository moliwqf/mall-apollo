package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.UmsRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author moli
 * @time 2024-03-30 10:37:18
 * @description 后台用户和角色关联查询
 */
@Repository
public interface UmsAdminRoleRelationDao {
    /**
     * 根据后台用户id 获取角色信息
     * @param adminId 后台用户id
     * @return 角色信息
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);
}
