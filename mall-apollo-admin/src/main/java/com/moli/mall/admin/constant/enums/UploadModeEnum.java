package com.moli.mall.admin.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author moli
 * @time 2024-04-03 12:01:26
 * @description 上传模式枚举类
 */
@AllArgsConstructor
@Getter
public enum UploadModeEnum {

    MINIO("minio", "minioUploadStrategy", "minio上传文件接口");

    private final String mode;

    private final String serviceName;

    private final String desc;

    private static final Map<String, UploadModeEnum> UPLOAD_MODE_ENUM_MAP;

    static {
        UPLOAD_MODE_ENUM_MAP = Arrays.stream(UploadModeEnum.values())
                .collect(Collectors.toMap(UploadModeEnum::getMode, Function.identity()));
    }

    public static String getServiceName(String mode) {
        return UPLOAD_MODE_ENUM_MAP.getOrDefault(mode, MINIO).getServiceName();
    }
}
