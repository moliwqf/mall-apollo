package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.UmsResource;
import com.moli.mall.mbg.model.UmsRoleResourceRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-01 15:13:42
 * @description 角色资源联系dao层
 */
@Repository
public interface UmsRoleResourceRelationDao {
    /**
     * 根据角色id获取资源
     * @param roleId 角色id
     * @return 资源信息
     */
    List<UmsResource> listResourcesByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量添加资源信息
     */
    int insertList(@Param("relationList") List<UmsRoleResourceRelation> relationList);
}
