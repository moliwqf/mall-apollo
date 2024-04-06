package com.moli.mall.auth.service.impl;

import com.moli.mall.auth.service.UmsAdminService;
import com.moli.mall.auth.domain.SecurityUser;
import com.moli.mall.auth.service.UmsMemberService;
import com.moli.mall.common.constant.AuthConstant;
import com.moli.mall.common.constant.MessageConstant;
import com.moli.mall.common.dto.UserDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-03-30 10:20:29
 * @description 查询数据库中的用户信息
 */
@Service("userDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UmsAdminService umsAdminService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private UmsMemberService umsMemberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = request.getParameter(AuthConstant.CLIENT_ID_PARAM);
        if (!StringUtils.hasLength(username)) return null;
        UserDto userDto = null;
        if (AuthConstant.ADMIN_CLIENT_ID.equals(clientId)) {
            userDto = umsAdminService.loadUserByUsername(username);
        } else if (AuthConstant.PORTAL_CLIENT_ID.equals(clientId)) {
            userDto = umsMemberService.loadUserByUsername(username);
        }
        if (Objects.isNull(userDto)) throw new RuntimeException(MessageConstant.USERNAME_PASSWORD_ERROR);
        userDto.setClientId(clientId);
        return checkUser(userDto);
    }

    @NotNull
    private static SecurityUser checkUser(UserDto userDto) {
        SecurityUser securityUser = new SecurityUser(userDto);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIAL_EXPIRED);
        }
        return securityUser;
    }
}
