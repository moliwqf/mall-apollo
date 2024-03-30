package com.moli.mall.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.moli.mall.admin.dao.UmsAdminRoleRelationDao;
import com.moli.mall.admin.dao.UmsRoleDao;
import com.moli.mall.admin.dto.UmsAdminParams;
import com.moli.mall.admin.service.AuthService;
import com.moli.mall.admin.service.UmsAdminCacheService;
import com.moli.mall.admin.service.UmsAdminService;
import com.moli.mall.admin.vo.UmsAdminNameIconWithMenusAndRolesVo;
import com.moli.mall.common.constant.AuthConstant;
import com.moli.mall.common.constant.ResultCode;
import com.moli.mall.common.constant.UserStatus;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.UmsAdminMapper;
import com.moli.mall.mbg.mapper.UmsAdminRoleRelationMapper;
import com.moli.mall.mbg.model.UmsAdmin;
import com.moli.mall.mbg.model.UmsAdminExample;
import com.moli.mall.mbg.model.UmsMenu;
import com.moli.mall.mbg.model.UmsRole;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-03-29 22:35:31
 * @description 后台用户服务实现层
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    @Resource
    private UmsAdminMapper umsAdminMapper;

    @Resource
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;

    @Resource
    private UmsAdminRoleRelationDao umsAdminRoleRelationDao;

    @Resource
    private UmsRoleDao umsRoleDao;

    @Resource
    private AuthService authService;

    @Resource
    private HttpServletRequest request;

    @Override
    public CommonResult<?> login(String username, String password) {
        if (!StrUtil.isAllNotEmpty(username, password)) {
            throw new RuntimeException("用户名或密码不能为空~"); // TODO 自定义异常信息
        }
        Map<String, String> params = new HashMap<>();
        params.put(AuthConstant.CLIENT_ID_PARAM, AuthConstant.ADMIN_CLIENT_ID);
        params.put(AuthConstant.CLIENT_SECRET_PARAM, AuthConstant.CLIENT_SECRETE_KEY);
        params.put(AuthConstant.GRANT_TYPE_PARAM, AuthConstant.PASSWORD_GRANT_TYPE);
        params.put(AuthConstant.USERNAME_PARAM, username);
        params.put(AuthConstant.PASSWORD_PARAM, password);
        // 远程调用auth服务进行验证获取token TODO
        return authService.getAccessToken(params);
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample adminExample = new UmsAdminExample();
        adminExample.createCriteria().andUsernameLike(username);
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(adminExample);
        if (!CollectionUtils.isEmpty(adminList)) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return umsAdminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        // 获取后台用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (Objects.nonNull(admin)) {
            UserDto userDto = BeanCopyUtil.copyBean(admin, UserDto.class);
            // 获取角色信息
            List<UmsRole> roleList = getRoleList(admin.getId());
            if (!CollectionUtils.isEmpty(roleList) && Objects.nonNull(userDto)) {
                List<String> roles = roleList.stream().map(role -> role.getId() + "_" + role.getName()).collect(Collectors.toList());
                userDto.setRoles(roles);
            }
            return userDto;
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminParams umsAdminParams) {
        UmsAdmin umsAdmin = BeanCopyUtil.copyBean(umsAdminParams, UmsAdmin.class);
        assert umsAdmin != null;
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(UserStatus.NORMAL.getCode());

        // 查询是否存在该用户
        UmsAdminExample adminExample = new UmsAdminExample();
        adminExample.createCriteria().andUsernameEqualTo(umsAdminParams.getUsername());
        List<UmsAdmin> umsAdmins = umsAdminMapper.selectByExample(adminExample);
        if (!CollectionUtils.isEmpty(umsAdmins)) {
            return null;
        }

        // 密码进行加密操作
        String newPassword = BCrypt.hashpw(umsAdminParams.getPassword());
        umsAdmin.setPassword(newPassword);
        umsAdminMapper.insert(umsAdmin);

        return umsAdmin;
    }

    @Override
    public UmsAdmin getCurrentAdmin() {
        String user = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(user)) {
            AssetsUtil.fail(ResultCode.UNAUTHORIZED);
        }
        UmsAdmin admin = JSONUtil.toBean(user, UmsAdmin.class);
        // 登录用户放入缓存 - cache aside pattern
        UmsAdminCacheService umsAdminCacheService = getUmsAdminCacheService();
        UmsAdmin currentAdmin = umsAdminCacheService.getAdmin(admin.getId());
        if (Objects.isNull(currentAdmin)) {
            // 查询数据库
            currentAdmin = umsAdminMapper.selectByPrimaryKey(admin.getId());
            umsAdminCacheService.setAdmin(admin.getId(), currentAdmin);
        }
        return currentAdmin;
    }

    @Override
    public UmsAdminCacheService getUmsAdminCacheService() {
        return SpringUtil.getBean(UmsAdminCacheService.class);
    }

    @Override
    public CommonResult<UmsAdminNameIconWithMenusAndRolesVo> info() {
        UmsAdmin currentAdmin = getCurrentAdmin();
        // 获取菜单信息
        List<UmsMenu> menuList = umsRoleDao.readMenuList(currentAdmin.getId());
        // 获取权限信息
        List<UmsRole> roleList = getRoleList(currentAdmin.getId());
        List<String> roleNameList = Optional.ofNullable(roleList)
                .orElse(new ArrayList<>())
                .stream().map(UmsRole::getName)
                .collect(Collectors.toList());
        UmsAdminNameIconWithMenusAndRolesVo adminVo = UmsAdminNameIconWithMenusAndRolesVo.builder()
                .username(currentAdmin.getUsername())
                .icon(currentAdmin.getIcon())
                .menuList(menuList)
                .roleList(roleNameList)
                .build();
        return CommonResult.success(adminVo);
    }
}
