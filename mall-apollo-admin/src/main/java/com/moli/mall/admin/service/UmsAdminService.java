package com.moli.mall.admin.service;

import com.moli.mall.admin.dto.UmsAdminParams;
import com.moli.mall.admin.vo.UmsAdminNameIconWithMenusAndRolesVo;
import com.moli.mall.common.domain.CommonPage;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.mbg.model.UmsAdmin;
import com.moli.mall.mbg.model.UmsRole;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author moli
 * @time 2024-03-29 22:35:08
 * @description 后台用户服务层
 */
public interface UmsAdminService {
    /**
     * 后台用户登录
     * @param username 用户名
     * @param password 密码
     */
    CommonResult<?> login(String username, String password);

    /**
     * 根据用户名获取后台用户信息
     * @param username 用户名
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 根据用户id获取用户的角色信息
     * @param id 用户id
     * @return 角色信息
     */
    List<UmsRole> getRoleList(Long id);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserDto loadUserByUsername(@RequestParam String username);

    /**
     * 注册后台用户
     * @param umsAdminParams 用户基础信息
     * @return 成功后返回该用户，否则为null
     */
    UmsAdmin register(UmsAdminParams umsAdminParams);

    /**
     * 根据当前请求获取用户信息
     * @return UmsAdmin
     */
    UmsAdmin getCurrentAdmin();

    /**
     * 获取缓存服务对象
     */
    UmsAdminCacheService getUmsAdminCacheService();

    /**
     * 获取用户界面信息
     */
    CommonResult<UmsAdminNameIconWithMenusAndRolesVo> info();

    /**
     * 分页获取后台用户信息
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param keyword 关键字
     * @return 分页信息
     */
    CommonResult<CommonPage<UmsAdmin>> list(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 更新后台用户的角色列表
     * @param adminId 用户id
     * @param roleIdList 角色列表id
     * @return 更新的行数
     */
    int updateRoleList(Long adminId, List<Long> roleIdList);

    /**
     * 更新指定的后台用户
     * @param adminId 用户id
     * @param umsAdmin 用户信息
     * @return 更新的行数
     */
    int updateAdmin(Long adminId, UmsAdmin umsAdmin);

    /**
     * 删除后台用户
     * @param adminId 用户id
     * @return 更新的行数
     */
    int deleteAdmin(Long adminId);
}
