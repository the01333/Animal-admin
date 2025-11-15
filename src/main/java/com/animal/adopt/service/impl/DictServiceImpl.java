package com.animal.adopt.service.impl;

import com.animal.adopt.mapper.PetMapper;
import com.animal.adopt.service.DictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 字典服务实现类
 * 从数据库查询字典数据并使用Redis缓存
 * 
 * @author Animal Adopt System
 * @date 2025-11-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictServiceImpl implements DictService {
    
    private final PetMapper petMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    // 缓存key
    private static final String CACHE_KEY_ALL = "dict:all";
    private static final String CACHE_KEY_PET_CATEGORY = "dict:pet_category";
    private static final String CACHE_KEY_GENDER = "dict:gender";
    private static final String CACHE_KEY_ADOPTION_STATUS = "dict:adoption_status";
    private static final String CACHE_KEY_HEALTH_STATUS = "dict:health_status";
    
    // 缓存过期时间：7天
    private static final long CACHE_EXPIRE_DAYS = 7;
    
    @Override
    public Map<String, Object> getAllDictData() {
        // 1. 尝试从缓存获取
        @SuppressWarnings("unchecked")
        Map<String, Object> cachedData = (Map<String, Object>) redisTemplate.opsForValue().get(CACHE_KEY_ALL);
        
        if (cachedData != null) {
            log.debug("从缓存获取所有字典数据");
            return cachedData;
        }
        
        // 2. 缓存未命中，查询数据库
        log.info("缓存未命中，从数据库查询所有字典数据");
        Map<String, Object> result = new HashMap<>();
        
        result.put("petCategories", getPetCategories());
        result.put("genders", getGenders());
        result.put("adoptionStatuses", getAdoptionStatuses());
        result.put("healthStatuses", getHealthStatuses());
        
        // 3. 写入缓存
        redisTemplate.opsForValue().set(CACHE_KEY_ALL, result, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        log.debug("所有字典数据已缓存");
        
        return result;
    }
    
    @Override
    public Map<String, String> getPetCategories() {
        // 1. 尝试从缓存获取
        @SuppressWarnings("unchecked")
        Map<String, String> cachedData = (Map<String, String>) redisTemplate.opsForValue().get(CACHE_KEY_PET_CATEGORY);
        
        if (cachedData != null) {
            log.debug("从缓存获取宠物类型字典");
            return cachedData;
        }
        
        // 2. 从数据库查询
        log.debug("从数据库查询宠物类型");
        List<String> categories = petMapper.selectDistinctCategories();
        
        // 3. 转换为Map并添加中文标签
        Map<String, String> result = new LinkedHashMap<>();
        for (String category : categories) {
            result.put(category, getCategoryLabel(category));
        }
        
        // 4. 写入缓存
        if (!result.isEmpty()) {
            redisTemplate.opsForValue().set(CACHE_KEY_PET_CATEGORY, result, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            log.debug("宠物类型字典已缓存，数量: {}", result.size());
        }
        
        return result;
    }
    
    @Override
    public Map<Integer, String> getGenders() {
        // 1. 尝试从缓存获取
        @SuppressWarnings("unchecked")
        Map<Integer, String> cachedData = (Map<Integer, String>) redisTemplate.opsForValue().get(CACHE_KEY_GENDER);
        
        if (cachedData != null) {
            log.debug("从缓存获取性别字典");
            return cachedData;
        }
        
        // 2. 性别是固定的，直接构建
        Map<Integer, String> result = new LinkedHashMap<>();
        result.put(0, "未知");
        result.put(1, "公");
        result.put(2, "母");
        
        // 3. 写入缓存
        redisTemplate.opsForValue().set(CACHE_KEY_GENDER, result, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        log.debug("性别字典已缓存");
        
        return result;
    }
    
    @Override
    public Map<String, String> getAdoptionStatuses() {
        // 1. 尝试从缓存获取
        @SuppressWarnings("unchecked")
        Map<String, String> cachedData = (Map<String, String>) redisTemplate.opsForValue().get(CACHE_KEY_ADOPTION_STATUS);
        
        if (cachedData != null) {
            log.debug("从缓存获取领养状态字典");
            return cachedData;
        }
        
        // 2. 从数据库查询
        log.debug("从数据库查询领养状态");
        List<String> statuses = petMapper.selectDistinctAdoptionStatuses();
        
        // 3. 转换为Map并添加中文标签
        Map<String, String> result = new LinkedHashMap<>();
        for (String status : statuses) {
            result.put(status, getAdoptionStatusLabel(status));
        }
        
        // 4. 写入缓存
        if (!result.isEmpty()) {
            redisTemplate.opsForValue().set(CACHE_KEY_ADOPTION_STATUS, result, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            log.debug("领养状态字典已缓存，数量: {}", result.size());
        }
        
        return result;
    }
    
    @Override
    public Map<String, String> getHealthStatuses() {
        // 1. 尝试从缓存获取
        @SuppressWarnings("unchecked")
        Map<String, String> cachedData = (Map<String, String>) redisTemplate.opsForValue()
                .get(CACHE_KEY_HEALTH_STATUS);
        
        if (cachedData != null) {
            log.debug("从缓存获取健康状态字典");
            return cachedData;
        }
        
        // 2. 健康状态是固定的，直接构建
        Map<String, String> result = new LinkedHashMap<>();
        result.put("healthy", "健康");
        result.put("sick", "生病");
        result.put("injured", "受伤");
        result.put("recovering", "康复中");
        
        // 3. 写入缓存
        redisTemplate.opsForValue().set(CACHE_KEY_HEALTH_STATUS, result, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        log.debug("健康状态字典已缓存");
        
        return result;
    }
    
    @Override
    public void refreshCache() {
        log.info("刷新所有字典缓存");
        
        redisTemplate.delete(CACHE_KEY_ALL);
        redisTemplate.delete(CACHE_KEY_PET_CATEGORY);
        redisTemplate.delete(CACHE_KEY_GENDER);
        redisTemplate.delete(CACHE_KEY_ADOPTION_STATUS);
        redisTemplate.delete(CACHE_KEY_HEALTH_STATUS);
    }
    
    /**
     * 获取宠物类型的中文标签
     */
    private String getCategoryLabel(String category) {
        return switch (category.toLowerCase()) {
            case "dog" -> "狗狗";
            case "cat" -> "猫咪";
            case "rabbit" -> "兔子";
            case "bird" -> "鸟类";
            case "other" -> "其他";
            default -> category;
        };
    }
    
    /**
     * 获取领养状态的中文标签
     */
    private String getAdoptionStatusLabel(String status) {
        return switch (status.toLowerCase()) {
            case "available" -> "可领养";
            case "reserved" -> "已预订";
            case "pending" -> "待审核";
            case "adopted" -> "已领养";
            default -> status;
        };
    }
}
