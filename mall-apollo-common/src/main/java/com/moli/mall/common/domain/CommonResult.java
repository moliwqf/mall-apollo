package com.moli.mall.common.domain;

import com.moli.mall.common.constant.ResultCode;

/**
 * @author moli
 * @time 2024-03-29 17:09:16
 * @description 公共结果类
 */
public class CommonResult<T> {

    private Integer code;
    private String message;
    private T data;

    public CommonResult() {}

    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> CommonResult<T> fail() {
        return fail(ResultCode.FAILED.getMessage());
    }

    public static <T> CommonResult<T> validateFail(String message) {
        return new CommonResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    public static <T> CommonResult<T> validateFail() {
        return validateFail(ResultCode.VALIDATE_FAILED.getMessage());
    }

    public static <T> CommonResult<T> unauthorized(String message) {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), message, null);
    }

    public static <T> CommonResult<T> unauthorized() {
        return validateFail(ResultCode.UNAUTHORIZED.getMessage());
    }

    public static <T> CommonResult<T> forbidden(String message) {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), message, null);
    }

    public static <T> CommonResult<T> forbidden() {
        return validateFail(ResultCode.FORBIDDEN.getMessage());
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
