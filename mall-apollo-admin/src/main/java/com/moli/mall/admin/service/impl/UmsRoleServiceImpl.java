package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.dao.UmsRoleDao;
import com.moli.mall.admin.dao.UmsRoleMenuRelationDao;
import com.moli.mall.admin.dao.UmsRoleResourceRelationDao;
import com.moli.mall.admin.service.UmsResourceService;
import com.moli.mall.admin.service.UmsRoleService;
import com.moli.mall.common.constant.CommonStatus;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.UmsRoleMapper;
import com.moli.mall.mbg.mapper.UmsRoleMenuRelationMapper;
import com.moli.mall.mbg.mapper.UmsRoleResourceRelationMapper;
import com.moli.mall.mbg.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-03-31 16:10:44
 * @description 用户角色服务实现
 */
@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Resource
    private UmsRoleMapper umsRoleMapper;

    @Resource
    private UmsRoleDao umsRoleDao;

    @Resource
    private UmsRoleMenuRelationMapper umsRoleMenuRelationMapper;

    @Resource
    private UmsRoleMenuRelationDao umsRoleMenuRelationDao;

    @Resource
    private UmsRoleResourceRelationDao umsRoleResourceRelationDao;

    @Resource
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;

    @Resource
    private UmsResourceService umsResourceService;

    @Override
    public List<UmsRole> listAll() {
        UmsRoleExample umsRoleExample = new UmsRoleExample();
        umsRoleExample.createCriteria().andStatusEqualTo(CommonStatus.NORMAL.getCode());
        return umsRoleMapper.selectByExample(umsRoleExample);
    }

    @Override
    public List<UmsRole> list(Integer pageNum, Integer pageSize, String keyword) {
        UmsRoleExample umsRoleExample = new UmsRoleExample();
        UmsRoleExample.Criteria criteria = umsRoleExample.createCriteria();
        criteria.andStatusEqualTo(CommonStatus.NORMAL.getCode());
        PageHelper.startPage(pageNum, pageSize);
        if (!StrUtil.isEmpty(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
        }
        return umsRoleMapper.selectByExample(umsRoleExample);
    }

    @Override
    @Transactional
    public int updateRole(Long roleId, UmsRole role) {
        UmsRole umsRole = umsRoleMapper.selectByPrimaryKey(roleId);
        if (Objects.isNull(umsRole)) {
            AssetsUtil.fail("不存在该角色信息！！");
        }
        role.setId(roleId);
        return umsRoleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    @Transactional
    public int createRole(UmsRole role) {
        UmsRoleExample umsRoleExample = new UmsRoleExample();
        umsRoleExample.createCriteria().andNameEqualTo(role.getName());
        List<UmsRole> umsRoles = umsRoleMapper.selectByExample(umsRoleExample);
        if (!CollectionUtils.isEmpty(umsRoles)) {
            AssetsUtil.fail("无法重复添加角色信息！！");
        }
        role.setCreateTime(new Date());
        role.setSort(0);
        role.setAdminCount(0);
        return umsRoleMapper.insert(role);
    }

    @Override
    @Transactional
    public int deleteRoles(List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return 0;
        }
        UmsRoleExample umsRoleExample = new UmsRoleExample();
        umsRoleExample.createCriteria().andIdIn(roleIdList);
        int count = umsRoleMapper.deleteByExample(umsRoleExample);

        // 重新查询所有的资源信息
        umsResourceService.initResourceRolesMap();
        return count;
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return umsRoleDao.listMenuByRoleId(roleId);
    }

    @Override
    @Transactional
    public int allocMenu(Long roleId, List<Long> menuIds) {
        // 判断角色是否存在
        UmsRole umsRole = umsRoleMapper.selectByPrimaryKey(roleId);
        if (Objects.isNull(umsRole)) {
            AssetsUtil.fail("角色不存在，非法分配菜单行为");
        }
        // 删除原有的菜单权限列表
        UmsRoleMenuRelationExample roleMenuRelationExample = new UmsRoleMenuRelationExample();
        roleMenuRelationExample.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleMenuRelationMapper.deleteByExample(roleMenuRelationExample);
        if (CollectionUtils.isEmpty(menuIds)) {
            return 0;
        }
        // 添加新的菜单权限
        List<UmsRoleMenuRelation> roleMenuRelationList = menuIds.stream()
                .map(menuId -> new UmsRoleMenuRelation(roleId, menuId))
                .collect(Collectors.toList());
        return umsRoleMenuRelationDao.insertList(roleMenuRelationList);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return umsRoleResourceRelationDao.listResourcesByRoleId(roleId);
    }

    @Override
    @Transactional
    public int allocResource(Long roleId, List<Long> resourceIds) {
        // 查询是否存在该角色
        UmsRole umsRole = umsRoleMapper.selectByPrimaryKey(roleId);
        if (Objects.isNull(umsRole)) {
            AssetsUtil.fail("不存在该角色信息！！");
        }
        // 删除原有的资源信息
        UmsRoleResourceRelationExample example = new UmsRoleResourceRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleResourceRelationMapper.deleteByExample(example);
        if (CollectionUtils.isEmpty(resourceIds)) {
            return 0;
        }

        // 添加新的资源信息
        List<UmsRoleResourceRelation> relationList = resourceIds.stream()
                .map(resourceId -> new UmsRoleResourceRelation(roleId, resourceId))
                .collect(Collectors.toList());

        // 将角色资源相关权限缓存到redis中
        umsResourceService.initResourceRolesMap();
        return umsRoleResourceRelationDao.insertList(relationList);
    }
}
