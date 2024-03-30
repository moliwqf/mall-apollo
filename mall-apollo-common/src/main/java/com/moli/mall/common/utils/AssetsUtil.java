package com.moli.mall.common.utils;

import com.moli.mall.common.constant.ResultCode;
import com.moli.mall.common.exception.GlobalException;

/**
 * @author moli
 * @time 2024-03-30 22:04:58
 * @description 断言工具类
 */
public class AssetsUtil {

    public static void fail(String message) {
        throw new GlobalException(message);
    }

    public static void fail(ResultCode resultCode) {
        throw new GlobalException(resultCode);
    }
}
