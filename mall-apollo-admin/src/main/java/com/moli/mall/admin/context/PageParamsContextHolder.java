package com.moli.mall.admin.context;

import com.moli.mall.common.domain.CommonPage;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author moli
 * @time 2024-03-31 17:27:13
 * @description 分页参数上下文
 */
public class PageParamsContextHolder {

    private static final ThreadLocal<CommonPage<?>> PAGE_HOLDER = new ThreadLocal<>();

    public static final String PAGE_SIZE = "pageSize";

    public static final String DEFAULT_PAGE_SIZE = "5";

    public static final String PAGE_NUM = "pageNum";

    public static final String DEFAULT_PAGE_NUM = "1";

    public static void setPage(Integer pageNum, Integer pageSize) {
        if (Objects.isNull(PAGE_HOLDER.get())) {
            PAGE_HOLDER.set(CommonPage.restPage(new ArrayList<>()));
        }
        CommonPage<?> page = PAGE_HOLDER.get();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
    }

    public static CommonPage<?> getPage() {
        return PAGE_HOLDER.get();
    }

    public static Integer getPageNum() {
        return PAGE_HOLDER.get().getPageNum();
    }

    public static Integer getPageSize() {
        return PAGE_HOLDER.get().getPageSize();
    }

    public static void removePage() {
        PAGE_HOLDER.remove();
    }
}
