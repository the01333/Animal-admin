package com.puxinxiaolin.adopt.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.puxinxiaolin.adopt.config.MinioConfig;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OssUrlService {
    private final MinioConfig ossConfig;

    public String normalizeUrl(String input) {
        if (input == null || input.isEmpty()) return input;
        String base = ossConfig.determineBaseUrl();
        if (base == null || base.isEmpty()) return input;
        String normalizedBase = base.endsWith("/") ? base : base + "/";
        String bucketName = ossConfig.getBucketName();

        String u = input.trim();
        if (u.startsWith("http://") || u.startsWith("https://")) {
            if (u.startsWith(normalizedBase)) {
                return u;
            }
            return normalizedBase + extractKeyFromUrl(u);
        }
        if (u.startsWith("/")) {
            u = u.substring(1);
        }
        if (!u.startsWith(bucketName + "/")) {
            u = bucketName + "/" + u;
        }
        return normalizedBase + u.substring(bucketName.length() + 1);
    }

    public String normalizeImages(String imagesJson) {
        if (imagesJson == null || imagesJson.isEmpty()) return imagesJson;
        try {
            List<String> list;
            if (imagesJson.trim().startsWith("[")) {
                list = JSONArray.parse(imagesJson).toList(String.class);
            } else if (imagesJson.contains(",")) {
                list = Arrays.asList(imagesJson.split(","));
            } else {
                list = List.of(imagesJson);
            }
            List<String> normalized = new ArrayList<>();
            for (String s : list) {
                normalized.add(normalizeUrl(s));
            }
            return JSON.toJSONString(normalized);
        } catch (Exception e) {
            return imagesJson;
        }
    }

    public void normalizePetVO(PetVO vo) {
        if (vo == null) return;
        vo.setCoverImage(normalizeUrl(vo.getCoverImage()));
        vo.setImages(normalizeImages(vo.getImages()));
    }

    private String extractKeyFromUrl(String url) {
        int slash = url.indexOf('/', url.indexOf("//") + 2);
        if (slash > 0 && slash < url.length() - 1) {
            String path = url.substring(slash + 1);
            String bucketPrefix = ossConfig.getBucketName() + "/";
            if (path.startsWith(bucketPrefix)) {
                return path.substring(bucketPrefix.length());
            }
            return path;
        }
        return url;
    }
}