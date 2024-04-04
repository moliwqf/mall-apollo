package com.moli.mall.admin.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-04 17:20:03
 * @description 退货状态
 */
@AllArgsConstructor
@Getter
public enum OrderReturnStatus {

    UN_HANDLE(0, "待处理"),

    RETURNING(1, "退货中"),

    FINISHED(2, "已完成"),

    REJECTED(3, "已拒绝");

    private final Integer status;

    private final String desc;

    private static final Map<Integer, OrderReturnStatus> RETURN_STATUS_MAP;

    static {
        RETURN_STATUS_MAP = Arrays.stream(OrderReturnStatus.values())
                .collect(Collectors.toMap(OrderReturnStatus::getStatus, Function.identity()));
    }

    public static OrderReturnStatus castStatus(Integer status) {
        return RETURN_STATUS_MAP.get(status);
    }
}
