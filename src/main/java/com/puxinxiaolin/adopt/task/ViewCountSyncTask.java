package com.puxinxiaolin.adopt.task;

import cn.hutool.core.collection.CollUtil;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.enums.ContentCategoryEnum;
import com.puxinxiaolin.adopt.mapper.GuideMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.mapper.StoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @Description: 浏览次数同步定时任务 <br />
 * 每5分钟将 redis 中的浏览次数增量同步到 MySQL 数据库
 * @Author: YCcLin
 * @Date: 2025/11/16 13:50
 */
@Component
@Slf4j
public class ViewCountSyncTask {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PetMapper petMapper;
    @Autowired
    private GuideMapper guideMapper;
    @Autowired
    private StoryMapper storyMapper;

    /**
     * 同步浏览量到 DB
     * <p>
     * 每5分钟同步一次浏览次数到数据库
     */
    // cron表达式: 0 */5 * * * ?
    //            秒 分 时 日 月 周
    @Scheduled(cron = "0 */5 * * * ?")
    public void syncViewCount() {
        log.info("========== 开始同步浏览次数到数据库 ==========");

        long startTime = System.currentTimeMillis();

        try {
            // 同步宠物浏览次数
            int petCount = syncPetViewCount();
            int contentCount = syncContentStats();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("========== 浏览次数同步完成 ==========");
            log.info("宠物: {}条, 内容: {}条, 耗时: {}ms", petCount, contentCount, duration);

        } catch (Exception e) {
            log.error("浏览次数同步失败", e);
        }
    }

    private int syncPetViewCount() {
        Set<String> keys = redisTemplate.keys(RedisConstant.PET_VIEW_COUNT_PREFIX + "*");
        if (CollUtil.isEmpty(keys)) {
            log.debug("没有需要同步的宠物浏览数据");
            return 0;
        }

        log.info("找到{}个宠物浏览记录需要同步", keys.size());

        int successCount = 0;
        int failCount = 0;

        for (String key : keys) {
            try {
                String countStr = redisTemplate.opsForValue().get(key);
                if (countStr == null || "0".equals(countStr)) {
                    continue;
                }

                int increment = Integer.parseInt(countStr);
                if (increment <= 0) {
                    continue;
                }

                String petIdStr = key.replace(RedisConstant.PET_VIEW_COUNT_PREFIX, "");
                Long petId = Long.parseLong(petIdStr);

                int updated = petMapper.incrementViewCount(petId, increment);

                if (updated > 0) {
                    redisTemplate.delete(key);
                    successCount++;
                    log.debug("同步宠物{}浏览次数成功: +{}", petId, increment);
                } else {
                    log.warn("同步宠物{}浏览次数失败: 数据库更新返回0", petId);
                    failCount++;
                }

            } catch (NumberFormatException e) {
                log.error("解析宠物ID或浏览次数失败: key={}", key, e);
                failCount++;
            } catch (Exception e) {
                log.error("同步宠物浏览次数失败: key={}", key, e);
                failCount++;
            }
        }

        if (failCount > 0) {
            log.warn("宠物浏览次数同步: 成功{}条, 失败{}条", successCount, failCount);
        }

        return successCount;
    }

    private int syncContentStats() {
        Set<String> keys = redisTemplate.keys(RedisConstant.CONTENT_STAT_HASH_PREFIX + "*");

        if (CollUtil.isEmpty(keys)) {
            log.debug("没有需要同步的内容互动数据");
            return 0;
        }

        log.info("找到{}个内容互动记录需要同步", keys.size());

        int successCount = 0;
        int failCount = 0;

        for (String key : keys) {
            try {
                String suffix = key.replace(RedisConstant.CONTENT_STAT_HASH_PREFIX, "");
                String[] parts = suffix.split(":");
                if (parts.length != 2) {
                    log.warn("内容互动Key格式不正确: {}", key);
                    continue;
                }

                ContentCategoryEnum category = ContentCategoryEnum.fromCode(parts[0]);
                Long contentId = Long.parseLong(parts[1]);

                Map<Object, Object> statMap = redisTemplate.opsForHash().entries(key);
                if (CollUtil.isEmpty(statMap)) {
                    redisTemplate.delete(key);
                    continue;
                }

                int viewIncrement = parseIncrement(statMap.get(RedisConstant.CONTENT_STAT_FIELD_VIEW));
                int likeIncrement = parseIncrement(statMap.get(RedisConstant.CONTENT_STAT_FIELD_LIKE));
                int favoriteIncrement = parseIncrement(statMap.get(RedisConstant.CONTENT_STAT_FIELD_FAVORITE));

                boolean hasChange = false;
                switch (category) {
                    case GUIDE -> {
                        if (viewIncrement > 0) {
                            guideMapper.incrementViewCount(contentId, viewIncrement);
                            hasChange = true;
                        }
                        if (likeIncrement != 0) {
                            guideMapper.incrementLikeCount(contentId, likeIncrement);
                            hasChange = true;
                        }
                        if (favoriteIncrement != 0) {
                            guideMapper.incrementFavoriteCount(contentId, favoriteIncrement);
                            hasChange = true;
                        }
                    }
                    case STORY -> {
                        if (viewIncrement > 0) {
                            storyMapper.incrementViewCount(contentId, viewIncrement);
                            hasChange = true;
                        }
                        if (likeIncrement != 0) {
                            storyMapper.incrementLikeCount(contentId, likeIncrement);
                            hasChange = true;
                        }
                        if (favoriteIncrement != 0) {
                            storyMapper.incrementFavoriteCount(contentId, favoriteIncrement);
                            hasChange = true;
                        }
                    }
                }

                if (hasChange) {
                    redisTemplate.delete(key);
                    successCount++;
                    log.debug("同步{}内容{}互动数据成功: view+{}, like{}, favorite{}",
                            category, contentId, viewIncrement, formatDelta(likeIncrement), formatDelta(favoriteIncrement));
                }

            } catch (Exception e) {
                log.error("同步内容互动数据失败: key={}", key, e);
                failCount++;
            }
        }

        if (failCount > 0) {
            log.warn("内容互动数据同步: 成功{}条, 失败{}条", successCount, failCount);
        }

        return successCount;
    }

    private int parseIncrement(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            log.warn("解析内容互动增量失败: value={}", value);
            return 0;
        }
    }

    private String formatDelta(int delta) {
        return delta > 0 ? "+" + delta : String.valueOf(delta);
    }

    /**
     * 手动触发同步（用于测试或紧急情况）
     */
    public void manualSync() {
        log.info("手动触发浏览次数同步");
        syncViewCount();
    }
}
