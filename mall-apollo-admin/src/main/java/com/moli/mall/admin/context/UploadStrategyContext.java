package com.moli.mall.admin.context;

import com.moli.mall.admin.constant.enums.UploadModeEnum;
import com.moli.mall.admin.vo.UploadedEntity;
import com.moli.mall.admin.context.upload.UploadStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

/**
 * @author moli
 * @time 2024-04-03 11:59:32
 * @description 上传策略上下文
 */
@Component
public class UploadStrategyContext {

    private final Map<String, UploadStrategy> uploadStrategyMap;

    @Value("${upload.mode:'minio'}")
    private String mode;

    public UploadStrategyContext(Map<String, UploadStrategy> uploadStrategyMap) {
        this.uploadStrategyMap = uploadStrategyMap;
    }

    /**
     * 根据文件上传
     *
     * @param file 文件
     * @param path 路径
     * @return 访问路径
     */
    public UploadedEntity executeUploadStrategy(MultipartFile file, String path) {
        return uploadStrategyMap.get(
                UploadModeEnum.getServiceName(mode)
        ).upload(file, path);
    }

    /**
     * 根据文件输入流上传
     *
     * @param fileName 文件名
     * @param is   输入流
     * @param path     路径
     * @return 访问路径
     */
    public UploadedEntity executeUploadStrategy(String fileName, InputStream is, String path) {
        return uploadStrategyMap.get(
                UploadModeEnum.getServiceName(mode)
        ).upload(is, path, fileName);
    }
}
