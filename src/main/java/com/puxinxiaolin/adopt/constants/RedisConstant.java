package com.puxinxiaolin.adopt.constants;

import com.puxinxiaolin.adopt.enums.ContentCategoryEnum;

/**
 * Redis Key 常量类
 * 统一管理所有Redis Key的前缀
 *
 * @author Animal Adopt System
 * @date 2025-11-10
 */
public class RedisConstant {

    // ========================== 验证码相关 ===============================

    /**
     * 邮箱验证码 key
     */
    public static String EMAIL_CODE_PREFIX = "code:email:";

    /**
     * 手机验证码 key
     */
    public static String PHONE_CODE_PREFIX = "code:phone:";


    // =========================== 宠物相关 ================================

    /**
     * 宠物浏览次数 key
     * 格式: pet:view:count:{petId}
     * 用途: 存储浏览次数增量, 定时同步到数据库
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
     * 用途: 缓存宠物点赞数, 提高查询性能
     */
    public static final String PET_LIKE_COUNT_PREFIX = "pet:like:count:";

    /**
     * 宠物收藏数缓存 key
     * 格式: pet:favorite:count:{petId}
     * 用途: 缓存宠物收藏数, 提高查询性能
     */
    public static final String PET_FAVORITE_COUNT_PREFIX = "pet:favorite:count:";

    // =========================== 内容（指南/故事）相关 ================================
    /**
     * 内容互动统计 Hash key
     * 格式: content:stat:{category}:{id}
     */
    public static final String CONTENT_STAT_HASH_PREFIX = "content:stat:";

    /**
     * 内容浏览限制 key（防刷）
     * 格式: content:view:limit:{category}:{id}:{userId}
     */
    public static final String CONTENT_VIEW_LIMIT_PREFIX = "content:view:limit:";

    /**
     * Hash 字段名称：浏览次数
     */
    public static final String CONTENT_STAT_FIELD_VIEW = "viewCount";

    /**
     * Hash 字段名称：点赞次数
     */
    public static final String CONTENT_STAT_FIELD_LIKE = "likeCount";

    /**
     * Hash 字段名称：收藏次数
     */
    public static final String CONTENT_STAT_FIELD_FAVORITE = "favoriteCount";

    // =========================== 用户相关 ================================
    public static final String VERIFICATION_CODE_PREFIX = "verification:code:";
    public static final String USER_TOKEN_PREFIX = "user:token:";

    // =========================== 访问统计相关 ================================
    /**
     * 每日 UV 记录 key
     * 格式: visit:uv:{yyyy-MM-dd}:{userId}
     */
    public static final String VISIT_UV_PREFIX = "visit:uv:";

    
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
     * 构建内容统计 Hash key
     */
    public static String buildContentStatKey(ContentCategoryEnum category, Long contentId) {
        return CONTENT_STAT_HASH_PREFIX + category.name() + ":" + contentId;
    }

    /**
     * 构建内容浏览限制 key
     */
    public static String buildContentViewLimitKey(ContentCategoryEnum category, Long contentId, Long userId) {
        return CONTENT_VIEW_LIMIT_PREFIX + category.name() + ":" + contentId + ":" + userId;
    }

    /**
     * 构建邮箱验证码 key
     *
     * @param targetEmail
     * @return
     */
    public static String buildEmailCodeKey(String targetEmail) {
        return EMAIL_CODE_PREFIX + targetEmail;
    }

    /**
     * 构建手机验证码 key
     */
    public static String buildPhoneCodeKey(String phone, String purpose) {
        return PHONE_CODE_PREFIX + purpose + ":" + phone;
    }

}
