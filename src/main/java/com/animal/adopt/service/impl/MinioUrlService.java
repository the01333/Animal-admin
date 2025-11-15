package com.animal.adopt.service.impl;

import com.animal.adopt.config.MinioConfig;
import com.animal.adopt.entity.vo.PetVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioUrlService {
    private final MinioConfig minioConfig;

    public String normalizeUrl(String input) {
        if (input == null || input.isEmpty()) return input;
        String base = minioConfig.getPublicBaseUrl();
        String bucket = minioConfig.getBucketName();
        if (base == null || base.isEmpty()) return input;
        String u = input;
        if (u.startsWith("http://") || u.startsWith("https://")) {
            int idx = u.indexOf("//");
            if (idx >= 0) {
                int slash = u.indexOf('/', idx + 2);
                if (slash > 0) {
                    String rest = u.substring(slash + 1);
                    int split = rest.indexOf('/');
                    if (split > 0) {
                        String b = rest.substring(0, split);
                        String key = rest.substring(split + 1);
                        return join(base, b, key);
                    }
                }
            }
        } else {
            return join(base, bucket, u);
        }
        return input;
    }

    public String normalizeImages(String imagesJson) {
        if (imagesJson == null || imagesJson.isEmpty()) return imagesJson;
        try {
            java.util.List<String> list;
            if (imagesJson.trim().startsWith("[")) {
                list = new com.alibaba.fastjson2.JSONArray().parse(imagesJson).toList(String.class);
            } else if (imagesJson.contains(",")) {
                list = java.util.Arrays.asList(imagesJson.split(","));
            } else {
                list = java.util.List.of(imagesJson);
            }
            java.util.List<String> normalized = new java.util.ArrayList<>();
            for (String s : list) {
                normalized.add(normalizeUrl(s));
            }
            return com.alibaba.fastjson2.JSON.toJSONString(normalized);
        } catch (Exception e) {
            return imagesJson;
        }
    }

    public void normalizePetVO(PetVO vo) {
        if (vo == null) return;
        vo.setCoverImage(normalizeUrl(vo.getCoverImage()));
        vo.setImages(normalizeImages(vo.getImages()));
    }

    private String join(String base, String bucket, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(base.endsWith("/") ? base.substring(0, base.length() - 1) : base);
        sb.append('/');
        sb.append(bucket);
        if (key.startsWith("/")) sb.append(key); else { sb.append('/').append(key); }
        return sb.toString();
    }
}