package com.animal.adopt.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.animal.adopt.common.ResultCode;
import com.animal.adopt.config.MinioConfig;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.service.FileUploadService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * MinIO文件上传服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioFileUploadServiceImpl implements FileUploadService {
    
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    
    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            // 确保bucket存在
            ensureBucketExists();
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = StrUtil.subAfter(originalFilename, ".", true);
            String fileName = IdUtil.simpleUUID() + "." + extension;
            String objectName = StrUtil.isBlank(folder) ? fileName : folder + "/" + fileName;
            
            // 上传文件
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioConfig.getBucketName())
                                .object(objectName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }
            
            // 返回文件访问URL
            String fileUrl = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;
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
            // 从URL中提取对象名称
            String objectName = extractObjectName(fileUrl);
            if (StrUtil.isBlank(objectName)) {
                return false;
            }
            
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );
            
            log.info("文件删除成功: {}", fileUrl);
            return true;
            
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return false;
        }
    }
    
    /**
     * 确保bucket存在
     */
    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build()
        );
        
        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .build()
            );
            
            // 设置bucket为公开访问
            String policy = """
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": {"AWS": "*"},
                            "Action": ["s3:GetObject"],
                            "Resource": ["arn:aws:s3:::%s/*"]
                        }
                    ]
                }
                """.formatted(minioConfig.getBucketName());
            
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .config(policy)
                            .build()
            );
        }
    }
    
    /**
     * 从URL中提取对象名称
     */
    private String extractObjectName(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return null;
        }
        
        String bucketPath = "/" + minioConfig.getBucketName() + "/";
        int index = fileUrl.indexOf(bucketPath);
        if (index == -1) {
            return null;
        }
        
        return fileUrl.substring(index + bucketPath.length());
    }
}
