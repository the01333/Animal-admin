package com.animal.adopt.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.animal.adopt.common.ResultCode;
import com.animal.adopt.config.AliyunOssConfig;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.service.FileUploadService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * MinIO 文件上传服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OssFileUploadServiceImpl implements FileUploadService {

    private final MinioClient minioClient;
    private final AliyunOssConfig ossConfig;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            ensureBucketExists();

            String originalFilename = file.getOriginalFilename();
            String extension = StrUtil.subAfter(originalFilename, ".", true);
            String fileName = IdUtil.simpleUUID() + (StrUtil.isBlank(extension) ? "" : "." + extension);
            String objectName = StrUtil.isBlank(folder) ? fileName : folder + "/" + fileName;
            objectName = sanitizeObjectName(objectName);

            try (InputStream inputStream = file.getInputStream()) {
                PutObjectArgs.Builder builder = PutObjectArgs.builder()
                        .bucket(ossConfig.getBucketName())
                        .object(objectName)
                        .stream(inputStream, file.getSize(), -1);
                String contentType = file.getContentType();
                if (StrUtil.isNotBlank(contentType)) {
                    builder.contentType(contentType);
                } else {
                    builder.contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                }
                minioClient.putObject(builder.build());
            }

            String fileUrl = buildFileUrl(objectName);
            log.info("文件上传成功: {}", fileUrl);
            return fileUrl;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            String objectName = ossConfig.extractObjectKeyFromUrl(fileUrl);
            if (StrUtil.isBlank(objectName)) {
                return false;
            }

            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(ossConfig.getBucketName())
                    .object(sanitizeObjectName(objectName))
                    .build());
            log.info("文件删除成功: {}", fileUrl);
            return true;

        } catch (Exception e) {
            log.error("文件删除失败", e);
            return false;
        }
    }

    private void ensureBucketExists() {
        String bucketName = ossConfig.getBucketName();
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new BusinessException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "校验/创建 MinIO 存储桶失败: " + e.getMessage());
        }
    }

    private String buildFileUrl(String objectName) {
        String sanitized = sanitizeObjectName(objectName);
        String base = ossConfig.determineBaseUrl();
        return base + (base.endsWith("/") ? "" : "/") + sanitized;
    }

    private String sanitizeObjectName(String objectName) {
        String value = StrUtil.nullToEmpty(objectName).trim();
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        String bucketPrefix = ossConfig.getBucketName() + "/";
        if (StrUtil.isNotBlank(ossConfig.getBucketName()) && value.startsWith(bucketPrefix)) {
            value = value.substring(bucketPrefix.length());
        }
        return value;
    }
}
