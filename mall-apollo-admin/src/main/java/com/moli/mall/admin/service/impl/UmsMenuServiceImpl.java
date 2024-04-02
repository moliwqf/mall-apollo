package com.moli.mall.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.UmsMenuService;
import com.moli.mall.admin.vo.UmsMenuNodeVo;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.UmsMenuMapper;
import com.moli.mall.mbg.model.UmsMenu;
import com.moli.mall.mbg.model.UmsMenuExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-01 14:20:13
 * @description 菜单服务实现层
 */
@Service
public class UmsMenuServiceImpl implements UmsMenuService {

    @Resource
    private UmsMenuMapper umsMenuMapper;

    @Override
    public List<UmsMenuNodeVo> treeList() {
        // 查询所有的菜单信息
        List<UmsMenu> menuList = umsMenuMapper.selectByExample(new UmsMenuExample());
        // 分级操作
        return menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> convertMenuNode(menu, menuList))
                .collect(Collectors.toList());
    }

    @Override
    public List<UmsMenu> list(Long parentId, Integer pageNum, Integer pageSize) {
        UmsMenuExample umsMenuExample = new UmsMenuExample();
        PageHelper.startPage(pageNum, pageSize);
        umsMenuExample.setOrderByClause("sort desc");
        umsMenuExample.createCriteria().andParentIdEqualTo(parentId);
        return umsMenuMapper.selectByExample(umsMenuExample);
    }

    @Override
    public UmsMenu info(Long menuId) {
        return umsMenuMapper.selectByPrimaryKey(menuId);
    }

    @Override
    @Transactional
    public int create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        return umsMenuMapper.insert(umsMenu);
    }

    @Override
    @Transactional
    public int update(Long menuId, UmsMenu umsMenu) {
        // 判断是否存在该菜单
        UmsMenu rawMenu = umsMenuMapper.selectByPrimaryKey(menuId);
        if (Objects.isNull(rawMenu)) {
            AssetsUtil.fail("菜单不存在！！");
        }
        umsMenu.setId(menuId);
        if (!rawMenu.getParentId().equals(umsMenu.getParentId())) {
            updateLevel(umsMenu);
        }
        return umsMenuMapper.updateByPrimaryKeySelective(umsMenu);
    }

    @Override
    @Transactional
    public int updateHidden(Long menuId, Integer hidden) {
        // 查询是否存在菜单
        UmsMenu rawMenu = umsMenuMapper.selectByPrimaryKey(menuId);
        if (Objects.isNull(rawMenu)) {
            AssetsUtil.fail("菜单不存在！！");
        }
        if (rawMenu.getHidden().equals(hidden)) {
            return 0;
        }
        rawMenu.setHidden(hidden);
        return umsMenuMapper.updateByPrimaryKey(rawMenu);
    }

    @Override
    @Transactional
    public int delete(Long menuId) {
        return umsMenuMapper.deleteByPrimaryKey(menuId);
    }

    /**
     * 修改菜单的层级
     */
    private void updateLevel(UmsMenu umsMenu) {
        if (Objects.isNull(umsMenu.getParentId()) || umsMenu.getParentId() == 0) {
            umsMenu.setLevel(0);
        } else {
            // 查询父级菜单
            UmsMenu parentMenu = umsMenuMapper.selectByPrimaryKey(umsMenu.getParentId());
            if (Objects.isNull(parentMenu)) {
                umsMenu.setLevel(0);
            } else {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            }
        }
    }

    /**
     * 菜单分级操作
     * @param parentMenu 父菜单
     * @param menuList 所有菜单集合
     * @return 分级菜单
     */
    private UmsMenuNodeVo convertMenuNode(UmsMenu parentMenu, List<UmsMenu> menuList) {
        UmsMenuNodeVo res = BeanCopyUtil.copyBean(parentMenu, UmsMenuNodeVo.class);
        List<UmsMenuNodeVo> children = menuList.stream()
                .filter(menu -> menu.getParentId().equals(parentMenu.getId()))
                .map(menu -> convertMenuNode(menu, menuList))
                .collect(Collectors.toList());
        assert res != null;
        res.setChildren(children);
        return res;
    }
}
