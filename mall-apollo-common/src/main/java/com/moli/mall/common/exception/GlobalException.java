package com.moli.mall.common.exception;

import com.moli.mall.common.constant.ResultCode;

/**
 * @author moli
 * @time 2024-03-30 22:00:40
 * @description 全局异常信息
 */
public class GlobalException extends RuntimeException {

    private ResultCode resultCode;

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public GlobalException(String message, Throwable t) {
        super(message, t);
    }

    public GlobalException(Throwable t) {
        super(t);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
