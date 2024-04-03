package com.moli.mall.admin.context.upload;

import cn.hutool.core.io.FileUtil;
import com.moli.mall.admin.vo.UploadedEntity;
import com.moli.mall.common.utils.AssetsUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * @author moli
 * @time 2024-04-03 11:39:39
 * @description 抽象上传接口
 */
public abstract class AbstractUploadStrategy implements UploadStrategy {

    protected String randomFileName(String rawFileName) {
        // 获取文件名后缀
        String suffix = FileUtil.getSuffix(rawFileName);
        // uuid获取文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        return fileName + "." + suffix;
    }

    @Override
    public UploadedEntity upload(MultipartFile file, String path) {
        UploadedEntity uploadedEntity = new UploadedEntity();
        if (Objects.isNull(file)) return uploadedEntity;
        String newFileName = randomFileName(file.getOriginalFilename());
        String url = getFileAccessUrl(path + "/" + newFileName);

        uploadedEntity.setName(newFileName);
        uploadedEntity.setUrl(url);
        if (exist(url)) {
            return uploadedEntity;
        }
        // 不存在就上传文件
        try {
            upload(file.getInputStream(), path, newFileName);
        } catch (IOException e) {
            AssetsUtil.fail("上传文件异常发生!!");
        }
        return uploadedEntity;
    }


    @Override
    public UploadedEntity upload(InputStream is, String path, String fileName) {
        UploadedEntity uploadedEntity = new UploadedEntity();
        try {
            uploadedEntity.setName(fileName);
            uploadedEntity.setUrl(getFileAccessUrl(path));
            uploadFile(is, path, fileName);
        } catch (Exception e) {
            AssetsUtil.fail("上传文件异常发生!!");
        }
        return uploadedEntity;
    }

    public abstract void uploadFile(InputStream is, String path, String fileName) throws Exception;

    public abstract String getFileAccessUrl(String path);

    public abstract boolean exist(String path);
}
