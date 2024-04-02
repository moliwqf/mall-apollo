package com.moli.mall.admin.service;

import com.moli.mall.mbg.model.UmsMenu;
import com.moli.mall.mbg.model.UmsResource;
import com.moli.mall.mbg.model.UmsRole;

import java.util.List;

/**
 * @author moli
 * @time 2024-03-31 16:10:28
 * @description 用户角色服务层
 */
public interface UmsRoleService {
    /**
     * 获取所有的角色信息
     * @return UmsRole
     */
    List<UmsRole> listAll();

    /**
     * 分页查询角色信息
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param keyword 模糊查询
     * @return UmsRole
     */
    List<UmsRole> list(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 更新角色信息
     * @param roleId 角色id
     * @param role 更新信息
     * @return 更新的行数
     */
    int updateRole(Long roleId, UmsRole role);

    /**
     * 添加新的角色信息
     * @param role 添加的角色信息
     * @return 添加的行数
     */
    int createRole(UmsRole role);

    /**
     * 批量删除角色信息
     * @param roleIdList id集合
     * @return 删除的行数
     */
    int deleteRoles(List<Long> roleIdList);

    /**
     * 获取角色拥有的菜单信息
     * @param roleId 角色id
     * @return 菜单信息
     */
    List<UmsMenu> listMenu(Long roleId);

    /**
     * 为角色分配菜单资源信息
     * @param roleId 角色id
     * @param menuIds 菜单id
     * @return 影响的行数
     */
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 根据角色id获取其资源信息
     * @param roleId 角色id
     * @return UmsResource
     */
    List<UmsResource> listResource(Long roleId);

    /**
     * 为角色分配资源信息
     * @param roleId 角色id
     * @param resourceIds 资源id列表
     * @return 影响的行数
     */
    int allocResource(Long roleId, List<Long> resourceIds);
}
