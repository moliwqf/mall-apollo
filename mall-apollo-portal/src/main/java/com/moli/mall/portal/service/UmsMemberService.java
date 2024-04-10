package com.moli.mall.portal.service;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.mbg.model.UmsMember;

/**
 * @author moli
 * @time 2024-04-06 14:18:52
 * @description 会员登录注册管理
 */
public interface UmsMemberService {
    /**
     * 会员登录
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    CommonResult<?> login(String username, String password);

    /**
     * 注册会员
     * @param username 用户名
     * @param password 密码
     * @param telephone 手机号
     * @param authCode 验证码
     */
    void register(String username, String password, String telephone, String authCode);

    /**
     * 根据用户名获取会员信息
     * @param username 用户名
     * @return 会员信息
     */
    UmsMember info(String username);

    /**
     * 根据用户名获取会员信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 获取当前登录的用户信息
     */
    UmsMember info();

    /**
     * 获取指定用户
     */
    UmsMember infoById(Long id);

    /**
     * 更新用户积分
     */
    void updateIntegration(Long id, Integer integration);
}
