package com.animal.adopt.service.impl;

import com.animal.adopt.constants.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 浏览次数服务
 * 使用Redis缓存浏览次数，定时同步到数据库
 * 
 * @author Animal Adopt System
 * @date 2025-11-10
 */
@Service
@Slf4j
public class ViewCountService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    /**
     * 增加宠物浏览次数
     * 
     * @param petId 宠物ID
     */
    public void incrementPetView(Long petId) {
        if (petId == null) {
            return;
        }
        
        String key = RedisConstant.PET_VIEW_COUNT_PREFIX + petId;
        
        try {
            // Redis的INCR命令是原子操作，线程安全
            redisTemplate.opsForValue().increment(key, 1);
            log.debug("宠物{}浏览次数+1", petId);
        } catch (Exception e) {
            log.error("增加宠物浏览次数失败: petId={}", petId, e);
        }
    }
    
    /**
     * 增加宠物浏览次数（带防刷限制）
     * 同一用户5分钟内只计数一次
     * 
     * @param petId 宠物ID
     * @param userId 用户ID（可为null，表示未登录用户）
     */
    public void incrementPetViewWithLimit(Long petId, Long userId) {
        if (petId == null) {
            return;
        }
        
        // 如果有用户ID，检查是否在限制时间内
        if (userId != null) {
            String limitKey = RedisConstant.PET_VIEW_LIMIT_PREFIX + petId + ":" + userId;
            
            Boolean exists = redisTemplate.hasKey(limitKey);
            if (exists) {
                log.debug("用户{}在5分钟内已浏览过宠物{}，不重复计数", userId, petId);
                return;
            }
            
            // 设置限制（5分钟过期）
            redisTemplate.opsForValue().set(limitKey, "1", 5, TimeUnit.MINUTES);
        }
        
        // 增加浏览次数
        incrementPetView(petId);
    }
    
    /**
     * 增加文章浏览次数
     * 
     * @param articleId 文章ID
     */
    public void incrementArticleView(Long articleId) {
        if (articleId == null) {
            return;
        }
        
        String key = RedisConstant.ARTICLE_VIEW_COUNT_PREFIX + articleId;
        
        try {
            redisTemplate.opsForValue().increment(key, 1);
            log.debug("文章{}浏览次数+1", articleId);
        } catch (Exception e) {
            log.error("增加文章浏览次数失败: articleId={}", articleId, e);
        }
    }
    
    /**
     * 增加文章浏览次数（带防刷限制）
     * 
     * @param articleId 文章ID
     * @param userId 用户ID
     */
    public void incrementArticleViewWithLimit(Long articleId, Long userId) {
        if (articleId == null) {
            return;
        }
        
        if (userId != null) {
            String limitKey = RedisConstant.ARTICLE_VIEW_LIMIT_PREFIX + articleId + ":" + userId;
            
            Boolean exists = redisTemplate.hasKey(limitKey);
            if (exists) {
                log.debug("用户{}在5分钟内已浏览过文章{}，不重复计数", userId, articleId);
                return;
            }
            
            redisTemplate.opsForValue().set(limitKey, "1", 5, TimeUnit.MINUTES);
        }
        
        incrementArticleView(articleId);
    }
    
    /**
     * 获取宠物的Redis增量浏览次数
     * 
     * @param petId 宠物ID
     * @return Redis中的增量值
     */
    public int getPetViewIncrement(Long petId) {
        if (petId == null) {
            return 0;
        }
        
        String key = RedisConstant.PET_VIEW_COUNT_PREFIX + petId;
        String value = redisTemplate.opsForValue().get(key);
        
        if (value == null) {
            return 0;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("解析Redis浏览次数失败: key={}, value={}", key, value, e);
            return 0;
        }
    }
    
    /**
     * 获取文章的Redis增量浏览次数
     * 
     * @param articleId 文章ID
     * @return Redis中的增量值
     */
    public int getArticleViewIncrement(Long articleId) {
        if (articleId == null) {
            return 0;
        }
        
        String key = RedisConstant.ARTICLE_VIEW_COUNT_PREFIX + articleId;
        String value = redisTemplate.opsForValue().get(key);
        
        if (value == null) {
            return 0;
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.error("解析Redis浏览次数失败: key={}, value={}", key, value, e);
            return 0;
        }
    }
}
