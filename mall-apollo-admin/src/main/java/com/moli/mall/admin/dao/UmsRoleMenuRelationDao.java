package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.UmsRoleMenuRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-01 14:51:30
 * @description 角色菜单dao层
 */
@Repository
public interface UmsRoleMenuRelationDao {
    /**
     * 批量添加角色菜单权限列表
     */
    int insertList(@Param("roleMenuRelationList") List<UmsRoleMenuRelation> roleMenuRelationList);
}
