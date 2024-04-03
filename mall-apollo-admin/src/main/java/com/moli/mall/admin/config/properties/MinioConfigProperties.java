package com.moli.mall.admin.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author moli
 * @time 2024-04-03 11:52:57
 * @description minio配置文件
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload.minio")
public class MinioConfigProperties {

    private String url;

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;
}
