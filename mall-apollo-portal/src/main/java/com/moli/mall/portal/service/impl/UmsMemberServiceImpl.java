package com.moli.mall.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.moli.mall.common.constant.AuthConstant;
import com.moli.mall.common.constant.ResultCode;
import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.common.utils.AssetsUtil;
import com.moli.mall.common.utils.BeanCopyUtil;
import com.moli.mall.mbg.mapper.UmsMemberMapper;
import com.moli.mall.mbg.model.UmsAdmin;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.mbg.model.UmsMemberExample;
import com.moli.mall.portal.service.AuthService;
import com.moli.mall.portal.service.UmsMemberCacheService;
import com.moli.mall.portal.service.UmsMemberService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-04-06 14:19:03
 * @description 会员登录注册管理
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Resource
    private UmsMemberMapper umsMemberMapper;

    @Resource
    private AuthService authService;

    @Resource
    private HttpServletRequest request;

    @Override
    public CommonResult<?> login(String username, String password) {
        if (!StrUtil.isAllNotEmpty(username, password)) {
            AssetsUtil.fail("用户名或密码不能为空~");
        }
        Map<String, String> params = new HashMap<>();
        params.put(AuthConstant.CLIENT_ID_PARAM, AuthConstant.PORTAL_CLIENT_ID);
        params.put(AuthConstant.CLIENT_SECRET_PARAM, AuthConstant.CLIENT_SECRETE_KEY);
        params.put(AuthConstant.GRANT_TYPE_PARAM, AuthConstant.PASSWORD_GRANT_TYPE);
        params.put(AuthConstant.USERNAME_PARAM, username);
        params.put(AuthConstant.PASSWORD_PARAM, password);
        return authService.getAccessToken(params);
    }

    @Override
    public void register(String username, String password, String telephone, String authCode) {

    }

    @Override
    public UmsMember info(String username) {
        UmsMemberExample umsMemberExample = new UmsMemberExample();
        umsMemberExample.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> umsMembers = umsMemberMapper.selectByExample(umsMemberExample);
        if (CollectionUtils.isEmpty(umsMembers)) return null;

        return umsMembers.get(0);
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        UmsMember info = this.info(username);
        if (Objects.isNull(info)) return null;

        UserDto ret = BeanCopyUtil.copyBean(info, UserDto.class);
        assert ret != null;
        ret.setRoles(CollUtil.toList("前台会员"));
        return ret;
    }

    @Override
    public UmsMember info() {
        String user = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if (StrUtil.isEmpty(user)) {
            AssetsUtil.fail(ResultCode.UNAUTHORIZED);
        }
        UmsMember member = JSONUtil.toBean(user, UmsMember.class);
        // 登录用户放入缓存 - cache aside pattern
        UmsMemberCacheService umsAdminCacheService = getUmsMemberCacheService();
        UmsMember currentMember = umsAdminCacheService.getMember(member.getId());
        if (Objects.isNull(currentMember)) {
            // 查询数据库
            currentMember = umsMemberMapper.selectByPrimaryKey(member.getId());
            umsAdminCacheService.setMember(member.getId(), currentMember);
        }
        return currentMember;
    }

    @Override
    public UmsMember infoById(Long id) {
        return umsMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void updateIntegration(Long id, Integer integration) {
        UmsMember member = new UmsMember();
        member.setId(id);
        member.setIntegration(integration);
        umsMemberMapper.updateByPrimaryKeySelective(member);
        getUmsMemberCacheService().delMember(id);
    }

    public UmsMemberCacheService getUmsMemberCacheService() {
        return SpringUtil.getBean(UmsMemberCacheService.class);
    }
}
