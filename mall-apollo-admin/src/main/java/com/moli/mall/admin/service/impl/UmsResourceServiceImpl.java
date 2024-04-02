package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.moli.mall.admin.service.UmsResourceService;
import com.moli.mall.common.constant.AuthConstant;
import com.moli.mall.common.service.RedisService;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.mbg.mapper.UmsResourceCategoryMapper;
import com.moli.mall.mbg.mapper.UmsResourceMapper;
import com.moli.mall.mbg.mapper.UmsRoleMapper;
import com.moli.mall.mbg.mapper.UmsRoleResourceRelationMapper;
import com.moli.mall.mbg.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-01 15:08:04
 * @description 资源服务实现层
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService {

    @Resource
    private UmsResourceMapper umsResourceMapper;

    @Resource
    private UmsResourceCategoryMapper umsResourceCategoryMapper;

    @Resource
    private UmsRoleMapper umsRoleMapper;

    @Resource
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;

    @Value("${spring.application.name:'mall-admin'}")
    private String applicationName;

    @Resource
    private RedisService redisService;

    @Override
    public List<UmsResource> listAll() {
        return umsResourceMapper.selectByExample(new UmsResourceExample());
    }

    @Override
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        UmsResourceExample umsResourceExample = new UmsResourceExample();
        UmsResourceExample.Criteria criteria = umsResourceExample.createCriteria();
        if (!Objects.isNull(categoryId)) {
            criteria.andCategoryIdEqualTo(categoryId);
        }
        if (StrUtil.isNotEmpty(nameKeyword)) {
            criteria.andNameLike("%" + nameKeyword + "%");
        }
        if (StrUtil.isNotEmpty(urlKeyword)) {
            criteria.andUrlLike("%" + urlKeyword + "%");
        }
        return umsResourceMapper.selectByExample(umsResourceExample);
    }

    @Override
    @Transactional
    public int create(UmsResource umsResource) {
        // 查询分类是否存在
        UmsResourceCategory category = umsResourceCategoryMapper.selectByPrimaryKey(umsResource.getCategoryId());
        if (Objects.isNull(category)) {
            AssetsUtil.fail("资源分类不存在，添加失败！！");
        }
        umsResource.setCreateTime(new Date());

        int count = umsResourceMapper.insert(umsResource);
        initResourceRolesMap();
        return count;
    }

    @Override
    @Transactional
    public int update(Long id, UmsResource umsResource) {
        // 查询源资源信息
        UmsResource rawResource = umsResourceMapper.selectByPrimaryKey(id);

        if (!Objects.isNull(umsResource.getCategoryId()) && !rawResource.getCategoryId().equals(umsResource.getCategoryId())) {
            // 查询分类是否存在
            UmsResourceCategory category = umsResourceCategoryMapper.selectByPrimaryKey(umsResource.getCategoryId());
            if (Objects.isNull(category)) {
                AssetsUtil.fail("资源分类不存在，更新失败！！");
            }
        }
        umsResource.setId(id);

        int count = umsResourceMapper.updateByPrimaryKeySelective(umsResource);
        initResourceRolesMap();
        return count;
    }

    @Override
    public UmsResource info(Long id) {
        return umsResourceMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int delete(Long id) {
        int count = umsResourceMapper.deleteByPrimaryKey(id);
        initResourceRolesMap();
        return count;
    }

    @Override
    public Map<String, List<String>> initResourceRolesMap() {
        // 获取所有的角色信息
        List<UmsRole> allRoles = umsRoleMapper.selectByExample(new UmsRoleExample());
        // 获取所有的资源信息
        List<UmsResource> allResources = umsResourceMapper.selectByExample(new UmsResourceExample());
        // 获取所有的关联信息
        List<UmsRoleResourceRelation> allRelations = umsRoleResourceRelationMapper.selectByExample(new UmsRoleResourceRelationExample());
        // 遍历所有的资源信息，跟角色信息进行匹配
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        allResources.forEach(resource -> {
            // 获取与该资源关联的角色信息
            Set<Long> relationRoleIdList = allRelations.stream()
                    .filter(r -> r.getResourceId().equals(resource.getId()))
                    .map(UmsRoleResourceRelation::getRoleId)
                    .collect(Collectors.toSet());
            List<String> roleInfoList = allRoles.stream()
                    .filter(r -> relationRoleIdList.contains(r.getId()))
                    .map(r -> r.getId() + "_" + r.getName())
                    .collect(Collectors.toList());
            // 添加进映射中
            resourceRolesMap.put("/" + applicationName + resource.getUrl(), roleInfoList);
        });
        // 删除原先的缓存信息
        redisService.del(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        // 添加新的缓存信息
        redisService.hSetAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRolesMap);
        return resourceRolesMap;
    }
}
