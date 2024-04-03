package com.moli.mall.admin.context.upload;

import com.moli.mall.admin.vo.UploadedEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author moli
 * @time 2024-04-03 12:32:07
 * @description 上传策略接口
 */
public interface UploadStrategy {

    /**
     * 根据文件上传
     * @return url
     */
    UploadedEntity upload(MultipartFile file, String path);

    /**
     * 根据流上传
     */
    UploadedEntity upload(InputStream is, String path, String fileName);

    /**
     * 判断文件是否存在
     * @param path 相对路径
     * @return 是否存在
     */
    boolean exist(String path);

    /**
     * 获取文件访问路径
     * @param path 相对路径
     * @return url
     */
    String getFileAccessUrl(String path);
}
