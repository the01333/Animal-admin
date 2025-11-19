package com.animal.adopt.constants;

/**
 * Redis Key 常量类
 * 统一管理所有Redis Key的前缀
 * 
 * @author Animal Adopt System
 * @date 2025-11-10
 */
public class RedisConstant {

    // =========================== 宠物相关 ================================
    
    /**
     * 宠物浏览次数 key
     * 格式: pet:view:count:{petId}
     * 用途: 存储浏览次数增量，定时同步到数据库
     */
    public static final String PET_VIEW_COUNT_PREFIX = "pet:view:count:";
    
    /**
     * 宠物浏览次数限制 key（防刷）
     * 格式: pet:view:limit:{petId}:{userId}
     * 用途: 限制同一用户5分钟内只计数一次
     */
    public static final String PET_VIEW_LIMIT_PREFIX = "pet:view:limit:";
    
    /**
     * 宠物点赞数缓存 key
     * 格式: pet:like:count:{petId}
     * 用途: 缓存宠物点赞数，提高查询性能
     */
    public static final String PET_LIKE_COUNT_PREFIX = "pet:like:count:";
    
    /**
     * 宠物收藏数缓存 key
     * 格式: pet:favorite:count:{petId}
     * 用途: 缓存宠物收藏数，提高查询性能
     */
    public static final String PET_FAVORITE_COUNT_PREFIX = "pet:favorite:count:";
    
    // =========================== 文章相关 ================================
    
    /**
     * 文章浏览次数 key
     * 格式: article:view:count:{articleId}
     * 用途: 存储浏览次数增量，定时同步到数据库
     */
    public static final String ARTICLE_VIEW_COUNT_PREFIX = "article:view:count:";
    
    /**
     * 文章浏览次数限制 key（防刷）
     * 格式: article:view:limit:{articleId}:{userId}
     * 用途: 限制同一用户5分钟内只计数一次
     */
    public static final String ARTICLE_VIEW_LIMIT_PREFIX = "article:view:limit:";
    
    /**
     * 文章点赞数缓存 key
     * 格式: article:like:count:{articleId}
     * 用途: 缓存文章点赞数，提高查询性能
     */
    public static final String ARTICLE_LIKE_COUNT_PREFIX = "article:like:count:";
    
    /**
     * 文章收藏数缓存 key
     * 格式: article:favorite:count:{articleId}
     * 用途: 缓存文章收藏数，提高查询性能
     */
    public static final String ARTICLE_FAVORITE_COUNT_PREFIX = "article:favorite:count:";
    
    // =========================== 用户相关 ================================
    public static final String VERIFICATION_CODE_PREFIX = "verification:code:";
    public static final String USER_TOKEN_PREFIX = "user:token:";
    
    // =========================== 字典相关 ================================
    public static final String DICT_ALL = "dict:all";
    public static final String DICT_PET_CATEGORY = "dict:pet_category";
    public static final String DICT_GENDER = "dict:gender";
    public static final String DICT_ADOPTION_STATUS = "dict:adoption_status";
    public static final String DICT_HEALTH_STATUS = "dict:health_status";
    
    // =========================== 工具方法 ================================
    
    /**
     * 构建宠物浏览次数key
     */
    public static String buildPetViewCountKey(Long petId) {
        return PET_VIEW_COUNT_PREFIX + petId;
    }
    
    /**
     * 构建宠物浏览限制key
     */
    public static String buildPetViewLimitKey(Long petId, Long userId) {
        return PET_VIEW_LIMIT_PREFIX + petId + ":" + userId;
    }
    
    /**
     * 构建宠物点赞数key
     */
    public static String buildPetLikeCountKey(Long petId) {
        return PET_LIKE_COUNT_PREFIX + petId;
    }
    
    /**
     * 构建宠物收藏数key
     */
    public static String buildPetFavoriteCountKey(Long petId) {
        return PET_FAVORITE_COUNT_PREFIX + petId;
    }
    
    /**
     * 构建文章浏览次数key
     */
    public static String buildArticleViewCountKey(Long articleId) {
        return ARTICLE_VIEW_COUNT_PREFIX + articleId;
    }
    
    /**
     * 构建文章浏览限制key
     */
    public static String buildArticleViewLimitKey(Long articleId, Long userId) {
        return ARTICLE_VIEW_LIMIT_PREFIX + articleId + ":" + userId;
    }
    
    /**
     * 构建文章点赞数key
     */
    public static String buildArticleLikeCountKey(Long articleId) {
        return ARTICLE_LIKE_COUNT_PREFIX + articleId;
    }
    
    /**
     * 构建文章收藏数key
     */
    public static String buildArticleFavoriteCountKey(Long articleId) {
        return ARTICLE_FAVORITE_COUNT_PREFIX + articleId;
    }
}
