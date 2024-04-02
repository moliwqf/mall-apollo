package com.moli.mall.admin.interceptor;

import cn.hutool.core.util.StrUtil;
import com.moli.mall.admin.context.PageParamsContextHolder;
import com.moli.mall.common.utils.AssetsUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.moli.mall.admin.context.PageParamsContextHolder.*;

/**
 * @author moli
 * @time 2024-03-31 17:20:01
 * @description 分页参数拦截器
 */
@SuppressWarnings("all")
@Component
public class PageParamsInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 获取分页参数
        String pageSize = Optional
                .ofNullable(StrUtil.isEmpty(request.getParameter(PAGE_SIZE)) ? null : request.getParameter(PAGE_SIZE))
                .orElse(DEFAULT_PAGE_SIZE);
        String pageNum = Optional
                .ofNullable(StrUtil.isEmpty(request.getParameter(PAGE_NUM)) ? null : request.getParameter(PAGE_NUM))
                .orElse(DEFAULT_PAGE_NUM);
        try {
            PageParamsContextHolder.setPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        } catch (NumberFormatException e) {
            AssetsUtil.fail("分页参数异常，不能进行转换~~~");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 删除分页信息
        PageParamsContextHolder.removePage();
    }
}
