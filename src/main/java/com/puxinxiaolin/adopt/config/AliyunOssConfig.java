package com.puxinxiaolin.adopt.config;

import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class AliyunOssConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String publicBaseUrl;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(applyProtocol(endpoint))
                .credentials(resolveAccessKey(), resolveSecretKey())
                .build();
    }

    public String determineBaseUrl() {
        if (StrUtil.isNotBlank(publicBaseUrl)) {
            String normalized = stripTrailingSlash(applyProtocol(publicBaseUrl));
            if (StrUtil.isNotBlank(bucketName)) {
                String lower = normalized.toLowerCase();
                String bucketSuffix = "/" + bucketName.toLowerCase();
                if (!lower.endsWith(bucketSuffix)) {
                    normalized = normalized + "/" + bucketName;
                }
            }
            return normalized;
        }
        String base = applyProtocol(endpoint);
        if (StrUtil.isBlank(base)) {
            return "";
        }
        String normalized = stripTrailingSlash(base);
        if (StrUtil.isNotBlank(bucketName) && !normalized.toLowerCase().endsWith("/" + bucketName.toLowerCase())) {
            normalized = normalized + "/" + bucketName;
        }
        return normalized;
    }

    private String applyProtocol(String value) {
        String trimmed = StrUtil.nullToEmpty(value).trim();
        if (StrUtil.isBlank(trimmed)) {
            throw new IllegalArgumentException("minio.endpoint must not be blank");
        }
        String normalized = trimmed.endsWith("/") ? trimmed.substring(0, trimmed.length() - 1) : trimmed;
        if (normalized.startsWith("http://") || normalized.startsWith("https://")) {
            return normalized;
        }
        return "http://" + normalized;
    }

    private String stripTrailingSlash(String value) {
        String result = StrUtil.nullToEmpty(value).trim();
        while (result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String resolveAccessKey() {
        return StrUtil.firstNonBlank(accessKey, accessKeyId);
    }

    private String resolveSecretKey() {
        return StrUtil.firstNonBlank(secretKey, accessKeySecret);
    }

    public String extractObjectKeyFromUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return url;
        }
        try {
            URI uri = new URI(url);
            String path = StrUtil.nullToEmpty(uri.getPath());
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (StrUtil.isBlank(path)) {
                return path;
            }
            String prefix = bucketName + "/";
            if (path.startsWith(prefix)) {
                return path.substring(prefix.length());
            }
            return path;
        } catch (URISyntaxException e) {
            return url;
        }
    }
}
