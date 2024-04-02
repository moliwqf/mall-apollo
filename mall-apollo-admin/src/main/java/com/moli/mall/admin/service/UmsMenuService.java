package com.moli.mall.admin.service;

import com.moli.mall.admin.vo.UmsMenuNodeVo;
import com.moli.mall.mbg.model.UmsMenu;

import java.util.List;

/**
 * @author moli
 * @time 2024-04-01 14:19:53
 * @description 菜单服务层
 */
public interface UmsMenuService {
    /**
     * 属性结构获取所有的菜单列表
     * @return UmsMenuNodeVo
     */
    List<UmsMenuNodeVo> treeList();

    /**
     * 根据父id获取菜单信息
     * @param parentId 父id
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return 菜单集合
     */
    List<UmsMenu> list(Long parentId, Integer pageNum, Integer pageSize);

    /**
     * 根据id获取菜单信息
     * @param menuId 菜单id
     * @return 菜单信息
     */
    UmsMenu info(Long menuId);

    /**
     * 创建新的菜单信息
     */
    int create(UmsMenu umsMenu);

    /**
     * 更新菜单
     * @param menuId 菜单id
     * @param umsMenu 新的信息
     */
    int update(Long menuId, UmsMenu umsMenu);

    /**
     * 更新菜单隐藏状态
     */
    int updateHidden(Long menuId, Integer hidden);

    /**
     * 删除菜单
     */
    int delete(Long menuId);
}
