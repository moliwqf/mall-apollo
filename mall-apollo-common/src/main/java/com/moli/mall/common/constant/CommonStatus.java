package com.moli.mall.common.constant;

import lombok.Getter;

/**
 * @author moli
 * @time 2024-03-30 20:34:12
 * @description 用户状态
 */
@Getter
public enum CommonStatus {

    NORMAL(1, "正常状态"),

    DISABLE(0, "禁用状态");

    private final Integer code;

    private final String desc;

    CommonStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
