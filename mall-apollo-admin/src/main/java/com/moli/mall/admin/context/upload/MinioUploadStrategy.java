package com.moli.mall.admin.context.upload;

import com.moli.mall.admin.config.properties.MinioConfigProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * @author moli
 * @time 2024-04-03 11:51:03
 * @description minio上传文件策略
 */
@Component(value = "minioUploadStrategy")
public class MinioUploadStrategy extends AbstractUploadStrategy {

    @Resource
    private MinioConfigProperties minioConfigProperties;

    @Override
    public void uploadFile(InputStream is, String path, String fileName) throws Exception {
        getMinioClient().putObject(
                PutObjectArgs.builder()
                        .bucket(minioConfigProperties.getBucketName())
                        .object(path + fileName)
                        .stream(is, is.available(), -1)
                        .build()
        );
    }

    @Override
    public boolean exist(String path) {
        try {
            getMinioClient()
                    .statObject(StatObjectArgs.builder()
                            .bucket(minioConfigProperties.getBucketName())
                            .object(path)
                            .build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getFileAccessUrl(String path) {
        return minioConfigProperties.getUrl() + minioConfigProperties.getBucketName() + "/" + path;
    }

    private MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(minioConfigProperties.getEndpoint())
                .credentials(minioConfigProperties.getAccessKey(), minioConfigProperties.getSecretKey())
                .build();
    }
}
