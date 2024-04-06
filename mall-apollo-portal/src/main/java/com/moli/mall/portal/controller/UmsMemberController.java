package com.moli.mall.portal.controller;

import com.moli.mall.common.domain.CommonResult;
import com.moli.mall.common.dto.UserDto;
import com.moli.mall.mbg.model.UmsMember;
import com.moli.mall.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-04-06 14:16:19
 * @description 会员登录注册管理
 */
@RestController
@Api(tags = "UmsMemberController", value = "会员登录注册管理")
@RequestMapping("/sso")
public class UmsMemberController {

    @Resource
    private UmsMemberService umsMemberService;

    @ApiOperation("获取会员信息")
    @RequestMapping(value = "/loadUserByUsername", method = RequestMethod.GET)
    public UserDto loadUserByUsername(@RequestParam("username") String username) {
        return umsMemberService.loadUserByUsername(username);
    }

    @ApiOperation("获取会员信息")
    @RequestMapping(value = "/info/username", method = RequestMethod.GET)
    public CommonResult<UmsMember> info(@RequestParam("username") String username) {
        UmsMember member = umsMemberService.info(username);
        return CommonResult.success(member);
    }

    @ApiOperation("获取当前登录会员信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<UmsMember> info() {
        UmsMember member = umsMemberService.info();
        return CommonResult.success(member);
    }

    @ApiOperation("会员注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResult<?> register(@RequestParam String username,
                                    @RequestParam String password,
                                    @RequestParam String telephone,
                                    @RequestParam String authCode) {
        umsMemberService.register(username, password, telephone, authCode);
        return CommonResult.success(null);
    }

    @ApiOperation("会员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult<?> login(@RequestParam String username,
                                 @RequestParam String password) {
        return umsMemberService.login(username, password);
    }
}
