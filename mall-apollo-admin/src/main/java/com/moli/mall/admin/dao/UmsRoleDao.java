package com.moli.mall.admin.dao;

import com.moli.mall.mbg.model.UmsMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author moli
 * @time 2024-03-30 22:35:12
 * @description 角色dao层
 */
@Repository
public interface UmsRoleDao {

    /**
     * 读取用户的菜单列表
     * @param adminId 用户id
     */
    List<UmsMenu> readMenuList(@Param("adminId") Long adminId);

    /**
     * 根据角色id获取其菜单信息
     * @param roleId 角色id
     * @return 菜单信息
     */
    List<UmsMenu> listMenuByRoleId(@Param("roleId") Long roleId);
}
