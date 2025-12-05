package com.puxinxiaolin.adopt.service.impl;

import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.enums.ContentCategoryEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;

/**
 * 浏览次数服务
 * <p>
 * 使用Redis缓存浏览次数, 定时同步到数据库
 * <p>
 * Redis仅存储增量, 持久值仍以数据库为准
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
            // Redis的INCR命令是原子操作, 线程安全
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
     * @param petId  宠物ID
     * @param userId 用户ID（可为null, 表示未登录用户）
     */
    public void incrementPetViewWithLimit(Long petId, Long userId) {
        if (petId == null) {
            return;
        }

        // 如果有用户ID, 检查是否在限制时间内
        if (userId != null) {
            String limitKey = RedisConstant.PET_VIEW_LIMIT_PREFIX + petId + ":" + userId;

            Boolean exists = redisTemplate.hasKey(limitKey);
            if (exists) {
                log.debug("用户{}在5分钟内已浏览过宠物{}, 不重复计数", userId, petId);
                return;
            }

            // 设置限制（5分钟过期）
            redisTemplate.opsForValue().set(limitKey, "1", 5, TimeUnit.MINUTES);
        }

        // 增加浏览次数
        incrementPetView(petId);
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
     * 增加内容浏览次数
     */
    public void incrementContentView(ContentCategoryEnum category, Long contentId) {
        if (category == null || contentId == null) {
            return;
        }
        incrementContentField(category, contentId, RedisConstant.CONTENT_STAT_FIELD_VIEW, 1);
    }

    /**
     * 增加内容浏览次数（带防刷限制, 登录用户5分钟内不重复计数）
     */
    public void incrementContentViewWithLimit(ContentCategoryEnum category, Long contentId, Long userId) {
        if (category == null || contentId == null) {
            return;
        }
        if (userId != null) {
            String limitKey = RedisConstant.buildContentViewLimitKey(category, contentId, userId);
            if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
                log.debug("用户{}在5分钟内已浏览过{}-{}, 不重复计数", userId, category, contentId);
                return;
            }
            redisTemplate.opsForValue().set(limitKey, "1", 5, TimeUnit.MINUTES);
        }
        incrementContentView(category, contentId);
    }

    /**
     * 获取内容的Redis增量浏览次数
     */
    public int getContentViewIncrement(ContentCategoryEnum category, Long contentId) {
        if (category == null || contentId == null) {
            return 0;
        }
        return getContentFieldIncrement(category, contentId, RedisConstant.CONTENT_STAT_FIELD_VIEW);
    }

    public void incrementContentLike(ContentCategoryEnum category, Long contentId, long delta) {
        incrementContentField(category, contentId, RedisConstant.CONTENT_STAT_FIELD_LIKE, delta);
    }

    public int getContentLikeIncrement(ContentCategoryEnum category, Long contentId) {
        return getContentFieldIncrement(category, contentId, RedisConstant.CONTENT_STAT_FIELD_LIKE);
    }

    public int getContentLikeCount(ContentCategoryEnum category, Long contentId, IntSupplier baseSupplier) {
        int base = baseSupplier == null ? 0 : baseSupplier.getAsInt();
        return base + getContentLikeIncrement(category, contentId);
    }

    public void incrementContentFavorite(ContentCategoryEnum category, Long contentId, long delta) {
        incrementContentField(category, contentId, RedisConstant.CONTENT_STAT_FIELD_FAVORITE, delta);
    }

    public int getContentFavoriteIncrement(ContentCategoryEnum category, Long contentId) {
        return getContentFieldIncrement(category, contentId, RedisConstant.CONTENT_STAT_FIELD_FAVORITE);
    }

    public int getContentFavoriteCount(ContentCategoryEnum category, Long contentId, IntSupplier baseSupplier) {
        int base = baseSupplier == null ? 0 : baseSupplier.getAsInt();
        return base + getContentFavoriteIncrement(category, contentId);
    }

    private void incrementContentField(ContentCategoryEnum category, Long contentId, String field, long delta) {
        if (category == null || contentId == null) {
            return;
        }
        String key = RedisConstant.buildContentStatKey(category, contentId);
        try {
            redisTemplate.opsForHash().increment(key, field, delta);
            log.debug("内容{}-{} 字段{} 增量{}", category, contentId, field, delta);
        } catch (Exception e) {
            log.error("更新内容统计失败: category={}, id={}, field={}, delta={}", category, contentId, field, delta, e);
        }
    }

    private int getContentFieldIncrement(ContentCategoryEnum category, Long contentId, String field) {
        String key = RedisConstant.buildContentStatKey(category, contentId);
        Object value = redisTemplate.opsForHash().get(key, field);
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            log.error("解析内容统计失败: key={}, field={}, value={}", key, field, value, e);
            return 0;
        }
    }

    // No need to materialize base values in Redis for like/favorite counts;
}
