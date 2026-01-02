package com.puxinxiaolin.adopt.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 * <p>
 * 统一封装 Redis 操作，支持泛型
 * </p>
 *
 * @author YCcLin
 */
@Slf4j
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ============================== 通用操作 ==============================

    /**
     * 设置过期时间
     */
    public boolean expire(String key, long timeout) {
        try {
            if (timeout > 0) {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置过期时间失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置过期时间
     */
    public boolean expire(String key, Duration duration) {
        try {
            if (duration != null && !duration.isNegative()) {
                redisTemplate.expire(key, duration);
            }
            return true;
        } catch (Exception e) {
            log.error("设置过期时间失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取过期时间（秒），-1 永久有效，-2 key不存在
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire != null ? expire : -2;
    }

    /**
     * 判断 key 是否存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("判断 key 是否存在失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     */
    public void delete(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(List.of(keys));
            }
        }
    }

    /**
     * 批量删除缓存
     */
    public void delete(Collection<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 根据前缀删除缓存
     */
    public void deleteByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 根据模式获取所有匹配的 key
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    // ============================== String 操作 ==============================

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (key == null) {
            return null;
        }
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取缓存失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 获取缓存，带默认值
     */
    public <T> T get(String key, T defaultValue) {
        T value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取缓存并转换为指定类型
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        if (clazz.isInstance(value)) {
            return (T) value;
        }
        // 处理数字类型转换
        if (value instanceof Number num) {
            if (clazz == Integer.class || clazz == int.class) {
                return (T) Integer.valueOf(num.intValue());
            } else if (clazz == Long.class || clazz == long.class) {
                return (T) Long.valueOf(num.longValue());
            } else if (clazz == Double.class || clazz == double.class) {
                return (T) Double.valueOf(num.doubleValue());
            }
        }
        // 处理字符串转数字
        if (value instanceof String str) {
            try {
                if (clazz == Integer.class || clazz == int.class) {
                    return (T) Integer.valueOf(str);
                } else if (clazz == Long.class || clazz == long.class) {
                    return (T) Long.valueOf(str);
                } else if (clazz == Double.class || clazz == double.class) {
                    return (T) Double.valueOf(str);
                }
            } catch (NumberFormatException e) {
                log.warn("字符串转数字失败: key={}, value={}", key, str);
            }
        }
        return null;
    }

    /**
     * 设置缓存
     */
    public <T> boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置缓存并设置过期时间（秒）
     */
    public <T> boolean set(String key, T value, long timeout) {
        try {
            if (timeout > 0) {
                redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置缓存并设置过期时间
     */
    public <T> boolean set(String key, T value, Duration duration) {
        try {
            if (duration != null && !duration.isNegative()) {
                redisTemplate.opsForValue().set(key, value, duration);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 如果不存在则设置（原子操作）
     */
    public <T> boolean setIfAbsent(String key, T value, long timeout) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS));
        } catch (Exception e) {
            log.error("setIfAbsent 失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 递增
     */
    public long increment(String key, long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("递增因子必须大于0");
        }
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result != null ? result : 0;
    }

    /**
     * 递增 1
     */
    public long increment(String key) {
        return increment(key, 1);
    }

    /**
     * 递减
     */
    public long decrement(String key, long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("递减因子必须大于0");
        }
        Long result = redisTemplate.opsForValue().increment(key, -delta);
        return result != null ? result : 0;
    }

    /**
     * 递减 1
     */
    public long decrement(String key) {
        return decrement(key, 1);
    }

    // ============================== Hash 操作 ==============================

    /**
     * 获取 Hash 中的值
     */
    @SuppressWarnings("unchecked")
    public <T> T hGet(String key, String field) {
        try {
            return (T) redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            log.error("hGet 失败, key: {}, field: {}", key, field, e);
            return null;
        }
    }

    /**
     * 获取 Hash 中所有键值对
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> hGetAll(String key) {
        try {
            return (Map<K, V>) redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("hGetAll 失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 设置 Hash 中的值
     */
    public <T> boolean hSet(String key, String field, T value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            log.error("hSet 失败, key: {}, field: {}", key, field, e);
            return false;
        }
    }

    /**
     * 设置 Hash 中的值并设置过期时间
     */
    public <T> boolean hSet(String key, String field, T value, long timeout) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            if (timeout > 0) {
                expire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            log.error("hSet 失败, key: {}, field: {}", key, field, e);
            return false;
        }
    }

    /**
     * 批量设置 Hash
     */
    public <K, V> boolean hSetAll(String key, Map<K, V> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("hSetAll 失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除 Hash 中的值
     */
    public void hDelete(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 判断 Hash 中是否存在该项
     */
    public boolean hHasKey(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * Hash 递增
     */
    public long hIncrement(String key, String field, long delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    // ============================== Set 操作 ==============================

    /**
     * 获取 Set 中所有值
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> sMembers(String key) {
        try {
            return (Set<T>) redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("sMembers 失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 判断 Set 中是否存在该值
     */
    public <T> boolean sIsMember(String key, T value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            log.error("sIsMember 失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 向 Set 中添加值
     */
    @SuppressWarnings("unchecked")
    public <T> long sAdd(String key, T... values) {
        try {
            Long result = redisTemplate.opsForSet().add(key, values);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("sAdd 失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 获取 Set 的大小
     */
    public long sSize(String key) {
        try {
            Long result = redisTemplate.opsForSet().size(key);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("sSize 失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 从 Set 中移除值
     */
    @SuppressWarnings("unchecked")
    public <T> long sRemove(String key, T... values) {
        try {
            Long result = redisTemplate.opsForSet().remove(key, values);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("sRemove 失败, key: {}", key, e);
            return 0;
        }
    }

    // ============================== List 操作 ==============================

    /**
     * 获取 List 中指定范围的元素
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> lRange(String key, long start, long end) {
        try {
            return (List<T>) redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("lRange 失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 获取 List 的大小
     */
    public long lSize(String key) {
        try {
            Long result = redisTemplate.opsForList().size(key);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("lSize 失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 获取 List 中指定索引的元素
     */
    @SuppressWarnings("unchecked")
    public <T> T lIndex(String key, long index) {
        try {
            return (T) redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("lIndex 失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 向 List 右侧添加元素
     */
    public <T> boolean lRightPush(String key, T value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("lRightPush 失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 向 List 右侧批量添加元素
     */
    public <T> boolean lRightPushAll(String key, List<T> values) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values.toArray());
            return true;
        } catch (Exception e) {
            log.error("lRightPushAll 失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置 List 中指定索引的值
     */
    public <T> boolean lSet(String key, long index, T value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("lSet 失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 从 List 中移除元素
     */
    public <T> long lRemove(String key, long count, T value) {
        try {
            Long result = redisTemplate.opsForList().remove(key, count, value);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("lRemove 失败, key: {}", key, e);
            return 0;
        }
    }
}
