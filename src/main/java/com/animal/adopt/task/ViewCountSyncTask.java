package com.animal.adopt.task;

import com.animal.adopt.constants.RedisKeyConstant;
import com.animal.adopt.mapper.ArticleMapper;
import com.animal.adopt.mapper.PetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 浏览次数同步定时任务
 * 每5分钟将Redis中的浏览次数增量同步到MySQL数据库
 *
 * @author Animal Adopt System
 * @date 2025-11-10
 */
@Component
@Slf4j
public class ViewCountSyncTask {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private ArticleMapper articleMapper;

    /**
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

            // 同步文章浏览次数
            int articleCount = syncArticleViewCount();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("========== 浏览次数同步完成 ==========");
            log.info("宠物: {}条, 文章: {}条, 耗时: {}ms", petCount, articleCount, duration);

        } catch (Exception e) {
            log.error("浏览次数同步失败", e);
        }
    }

    /**
     * 同步宠物浏览次数
     *
     * @return 同步的记录数
     */
    private int syncPetViewCount() {
        // 1. 获取所有宠物浏览计数的Key
        Set<String> keys = redisTemplate.keys(RedisKeyConstant.PET_VIEW_COUNT_PREFIX + "*");

        if (keys == null || keys.isEmpty()) {
            log.debug("没有需要同步的宠物浏览数据");
            return 0;
        }

        log.info("找到{}个宠物浏览记录需要同步", keys.size());

        int successCount = 0;
        int failCount = 0;

        // 2. 遍历每个Key
        for (String key : keys) {
            try {
                // 获取增量值
                String countStr = redisTemplate.opsForValue().get(key);
                if (countStr == null || "0".equals(countStr)) {
                    continue;
                }

                int increment = Integer.parseInt(countStr);
                if (increment <= 0) {
                    continue;
                }

                // 提取petId
                // key格式: pet:view:count:123
                String petIdStr = key.replace(RedisKeyConstant.PET_VIEW_COUNT_PREFIX, "");
                Long petId = Long.parseLong(petIdStr);

                // 3. 更新数据库（使用 += 操作）
                int updated = petMapper.incrementViewCount(petId, increment);

                if (updated > 0) {
                    // 4. 删除Redis中的Key（重要！避免重复同步）
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

    /**
     * 同步文章浏览次数
     *
     * @return 同步的记录数
     */
    private int syncArticleViewCount() {
        Set<String> keys = redisTemplate.keys(RedisKeyConstant.ARTICLE_VIEW_COUNT_PREFIX + "*");

        if (keys == null || keys.isEmpty()) {
            log.debug("没有需要同步的文章浏览数据");
            return 0;
        }

        log.info("找到{}个文章浏览记录需要同步", keys.size());

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

                String articleIdStr = key.replace(RedisKeyConstant.ARTICLE_VIEW_COUNT_PREFIX, "");
                Long articleId = Long.parseLong(articleIdStr);

                int updated = articleMapper.incrementViewCount(articleId, increment);

                if (updated > 0) {
                    redisTemplate.delete(key);
                    successCount++;

                    log.debug("同步文章{}浏览次数成功: +{}", articleId, increment);
                } else {
                    log.warn("同步文章{}浏览次数失败: 数据库更新返回0", articleId);
                    failCount++;
                }

            } catch (NumberFormatException e) {
                log.error("解析文章ID或浏览次数失败: key={}", key, e);
                failCount++;
            } catch (Exception e) {
                log.error("同步文章浏览次数失败: key={}", key, e);
                failCount++;
            }
        }

        if (failCount > 0) {
            log.warn("文章浏览次数同步: 成功{}条, 失败{}条", successCount, failCount);
        }

        return successCount;
    }

    /**
     * 手动触发同步（用于测试或紧急情况）
     * 可以通过接口调用此方法
     */
    public void manualSync() {
        log.info("手动触发浏览次数同步");
        syncViewCount();
    }
}
