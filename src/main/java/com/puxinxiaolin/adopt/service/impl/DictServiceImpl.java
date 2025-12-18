package com.puxinxiaolin.adopt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.constants.RedisConstant;
import com.puxinxiaolin.adopt.entity.dto.DictItemDTO;
import com.puxinxiaolin.adopt.entity.entity.DictItem;
import com.puxinxiaolin.adopt.entity.vo.DictItemVO;
import com.puxinxiaolin.adopt.enums.ContentCategoryEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.DictItemMapper;
import com.puxinxiaolin.adopt.mapper.PetMapper;
import com.puxinxiaolin.adopt.service.DictService;
import com.puxinxiaolin.adopt.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private final DictItemMapper dictItemMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TranslationService translationService;

    // 缓存过期时间
    private static final long CACHE_EXPIRE_DAYS = 7;

    @Override
    public Map<String, Object> getAllDictData() {
        // 1. 尝试从缓存获取
        @SuppressWarnings("unchecked")
        Map<String, Object> cachedData = (Map<String, Object>) redisTemplate.opsForValue().get(RedisConstant.DICT_ALL);

        if (cachedData != null) {
            log.debug("从缓存获取所有字典数据");
            return cachedData;
        }

        // 2. 缓存未命中, 查询数据库
        log.info("缓存未命中, 从数据库查询所有字典数据");
        Map<String, Object> result = new HashMap<>();

        result.put("petCategories", getPetCategories());
        result.put("genders", getGenders());
        result.put("adoptionStatuses", getAdoptionStatuses());
        result.put("healthStatuses", getHealthStatuses());
        result.put("articleCategories", getArticleCategories());

        // 3. 写入缓存
        redisTemplate.opsForValue().set(RedisConstant.DICT_ALL, result, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        log.debug("所有字典数据已缓存");

        return result;
    }

    @Override
    public Map<String, String> getPetCategories() {
        // 1. 尝试从缓存获取（使用Hash结构）
        try {
            Map<Object, Object> cachedData = redisTemplate.opsForHash().entries(RedisConstant.DICT_PET_CATEGORY);
            if (!cachedData.isEmpty()) {
                log.debug("从缓存获取宠物类型字典");
                return convertToStringMap(cachedData);
            }
        } catch (Exception e) {
            // 如果缓存中存储的是旧格式（String）, 则清除并重新生成
            log.warn("缓存格式错误, 清除缓存: {}", RedisConstant.DICT_PET_CATEGORY);
            redisTemplate.delete(RedisConstant.DICT_PET_CATEGORY);
        }

        // 2. 优先从通用字典表读取
        Map<String, String> result = loadDictFromTable("pet_category");

        // 3. 如果字典表没有配置, 则回退到从 t_pet 表中查询 distinct category
        if (result.isEmpty()) {
            log.debug("通用字典表未配置宠物类型, 回退到 t_pet 去重查询");
            List<String> categories = petMapper.selectDistinctCategories();
            for (String category : categories) {
                result.put(category, getCategoryLabel(category));
            }
        }

        // 4. 写入缓存（使用 Hash 结构）
        if (!result.isEmpty()) {
            redisTemplate.opsForHash().putAll(RedisConstant.DICT_PET_CATEGORY, result);
            redisTemplate.expire(RedisConstant.DICT_PET_CATEGORY, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            log.debug("宠物类型字典已缓存, 数量: {}", result.size());
        }

        return result;
    }

    @Override
    public Map<Integer, String> getGenders() {
        // 1. 尝试从缓存获取（使用Hash结构）
        try {
            Map<Object, Object> cachedData = redisTemplate.opsForHash().entries(RedisConstant.DICT_GENDER);
            if (!cachedData.isEmpty()) {
                log.debug("从缓存获取性别字典");
                return convertToIntegerMap(cachedData);
            }
        } catch (Exception e) {
            // 如果缓存中存储的是旧格式（String）, 则清除并重新生成
            log.warn("缓存格式错误, 清除缓存: {}", RedisConstant.DICT_GENDER);
            redisTemplate.delete(RedisConstant.DICT_GENDER);
        }

        // 2. 性别是固定的, 直接构建
        Map<String, String> result = new LinkedHashMap<>();
        result.put("0", "未知");
        result.put("1", "公");
        result.put("2", "母");

        // 3. 写入缓存（使用Hash结构）
        redisTemplate.opsForHash().putAll(RedisConstant.DICT_GENDER, result);
        redisTemplate.expire(RedisConstant.DICT_GENDER, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        log.debug("性别字典已缓存");

        // 转换为Integer Map返回
        Map<Integer, String> intResult = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : result.entrySet()) {
            intResult.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return intResult;
    }

    @Override
    public Map<String, String> getAdoptionStatuses() {
        // 1. 尝试从缓存获取（使用Hash结构）
        try {
            Map<Object, Object> cachedData = redisTemplate.opsForHash().entries(RedisConstant.DICT_ADOPTION_STATUS);
            if (!cachedData.isEmpty()) {
                log.debug("从缓存获取领养状态字典");
                return convertToStringMap(cachedData);
            }
        } catch (Exception e) {
            // 如果缓存中存储的是旧格式（String）, 则清除并重新生成
            log.warn("缓存格式错误, 清除缓存: {}", RedisConstant.DICT_ADOPTION_STATUS);
            redisTemplate.delete(RedisConstant.DICT_ADOPTION_STATUS);
        }

        // 2. 优先从通用字典表读取
        Map<String, String> result = loadDictFromTable("adoption_status");

        // 3. 如果字典表没有配置, 则回退到从 t_pet 表中查询 distinct adoption_status
        if (result.isEmpty()) {
            log.debug("通用字典表未配置领养状态, 回退到 t_pet 去重查询");
            List<String> statuses = petMapper.selectDistinctAdoptionStatuses();
            for (String status : statuses) {
                result.put(status, getAdoptionStatusLabel(status));
            }
        }

        // 4. 写入缓存（使用Hash结构）
        if (!result.isEmpty()) {
            redisTemplate.opsForHash().putAll(RedisConstant.DICT_ADOPTION_STATUS, result);
            redisTemplate.expire(RedisConstant.DICT_ADOPTION_STATUS, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            log.debug("领养状态字典已缓存, 数量: {}", result.size());
        }

        return result;
    }

    @Override
    public Map<String, String> getHealthStatuses() {
        // 1. 尝试从缓存获取（使用Hash结构）
        try {
            Map<Object, Object> cachedData = redisTemplate.opsForHash().entries(RedisConstant.DICT_HEALTH_STATUS);
            if (!cachedData.isEmpty()) {
                log.debug("从缓存获取健康状态字典");
                return convertToStringMap(cachedData);
            }
        } catch (Exception e) {
            // 如果缓存中存储的是旧格式（String）, 则清除并重新生成
            log.warn("缓存格式错误, 清除缓存: {}", RedisConstant.DICT_HEALTH_STATUS);
            redisTemplate.delete(RedisConstant.DICT_HEALTH_STATUS);
        }

        // 2. 优先从通用字典表读取
        Map<String, String> result = loadDictFromTable("health_status");

        // 3. 如果字典表没有配置, 则使用固定的默认值
        if (result.isEmpty()) {
            log.debug("通用字典表未配置健康状态, 使用默认配置");
            result.put("healthy", "健康");
            result.put("sick", "生病");
            result.put("injured", "受伤");
            result.put("recovering", "康复中");
        }

        // 4. 写入缓存（使用Hash结构）
        redisTemplate.opsForHash().putAll(RedisConstant.DICT_HEALTH_STATUS, result);
        redisTemplate.expire(RedisConstant.DICT_HEALTH_STATUS, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        log.debug("健康状态字典已缓存");

        return result;
    }

    @Override
    public Map<String, String> getArticleCategories() {
        // 1. 尝试从缓存获取（使用Hash结构）
        try {
            Map<Object, Object> cachedData = redisTemplate.opsForHash()
                    .entries(RedisConstant.DICT_ARTICLE_CATEGORY);
            if (!cachedData.isEmpty()) {
                log.debug("从缓存获取文章分类字典");
                return convertToStringMap(cachedData);
            }
        } catch (Exception e) {
            log.warn("缓存格式错误, 清除缓存: {}", RedisConstant.DICT_ARTICLE_CATEGORY);
            redisTemplate.delete(RedisConstant.DICT_ARTICLE_CATEGORY);
        }

        // 2. 优先从通用字典表读取
        Map<String, String> result = loadDictFromTable("article_category");

        // 3. 如果字典表没有配置, 则回退到 ContentCategoryEnum 默认值
        if (result.isEmpty()) {
            log.debug("通用字典表未配置文章分类, 使用 ContentCategoryEnum 默认配置");
            for (ContentCategoryEnum value : ContentCategoryEnum.values()) {
                result.put(value.name(), value.getLabel());
            }
        }

        // 4. 写入缓存
        redisTemplate.opsForHash().putAll(RedisConstant.DICT_ARTICLE_CATEGORY, result);
        redisTemplate.expire(RedisConstant.DICT_ARTICLE_CATEGORY, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        log.debug("文章分类字典已缓存");

        return result;
    }

    @Override
    public void refreshCache() {
        log.info("刷新所有字典缓存");

        redisTemplate.delete(RedisConstant.DICT_ALL);
        redisTemplate.delete(RedisConstant.DICT_PET_CATEGORY);
        redisTemplate.delete(RedisConstant.DICT_GENDER);
        redisTemplate.delete(RedisConstant.DICT_ADOPTION_STATUS);
        redisTemplate.delete(RedisConstant.DICT_HEALTH_STATUS);
        redisTemplate.delete(RedisConstant.DICT_ARTICLE_CATEGORY);
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
            case "hedgehog" -> "刺猬";
            case "squirrel" -> "松鼠";
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

    /**
     * 将 Hash 返回的 Map<Object, Object> 转换为 Map<String, String>
     */
    private Map<String, String> convertToStringMap(Map<Object, Object> objectMap) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : objectMap.entrySet()) {
            result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return result;
    }

    /**
     * 将 Hash 返回的 Map<Object, Object> 转换为 Map<Integer, String>
     */
    private Map<Integer, String> convertToIntegerMap(Map<Object, Object> objectMap) {
        Map<Integer, String> result = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : objectMap.entrySet()) {
            result.put(Integer.parseInt(String.valueOf(entry.getKey())), String.valueOf(entry.getValue()));
        }
        return result;
    }

    /**
     * 从通用字典表按照 dictType 读取字典项, 并转换为 Map<key, label>
     */
    private Map<String, String> loadDictFromTable(String dictType) {
        Map<String, String> result = new LinkedHashMap<>();

        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictItem::getDictType, dictType)
                .eq(DictItem::getStatus, 1)
                .eq(DictItem::getDeleted, 0)
                .orderByAsc(DictItem::getSortOrder)
                .orderByAsc(DictItem::getId);

        List<DictItem> items = dictItemMapper.selectList(wrapper);
        for (DictItem item : items) {
            result.put(item.getDictKey(), item.getDictLabel());
        }
        return result;
    }

    @Override
    public void ensureDictItems(String dictType, List<String> dictKeys) {
        if (dictType == null || dictType.isBlank() || dictKeys == null || dictKeys.isEmpty()) {
            return;
        }

        // 过滤空白键并去重
        List<String> normalizedKeys = dictKeys.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();
        if (normalizedKeys.isEmpty()) {
            return;
        }

        // 查询该类型下已有的所有字典项
        LambdaQueryWrapper<DictItem> allWrapper = new LambdaQueryWrapper<>();
        allWrapper.eq(DictItem::getDictType, dictType);
        List<DictItem> existingItems = dictItemMapper.selectList(allWrapper);
        Set<String> existingKeys = existingItems.stream()
                .map(DictItem::getDictKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        int sortOrder = existingItems.stream()
                .map(DictItem::getSortOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);

        boolean inserted = false;
        for (String key : normalizedKeys) {
            if (existingKeys.contains(key)) {
                continue;
            }
            DictItem item = new DictItem();
            item.setDictType(dictType);
            item.setDictKey(key);
            item.setDictLabel(key);
            item.setSortOrder(++sortOrder);
            item.setStatus(1);
            dictItemMapper.insert(item);
            inserted = true;
        }

        if (inserted) {
            // 有新增项时, 刷新缓存, 确保下次读取能拿到最新字典
            refreshCache();
        }
    }

    @Override
    public List<DictItemVO> listDictItems(String dictType) {
        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<>();
        if (dictType != null && !dictType.isBlank()) {
            wrapper.eq(DictItem::getDictType, dictType);
        }
        wrapper.orderByAsc(DictItem::getDictType)
                .orderByAsc(DictItem::getSortOrder)
                .orderByAsc(DictItem::getId);

        List<DictItem> items = dictItemMapper.selectList(wrapper);
        return items.stream()
                .map(this::toDictItemVO)
                .toList();
    }

    @Override
    public Long createDictItem(DictItemDTO dto) {
        DictItem item = new DictItem();
        item.setDictType(dto.getDictType());
        item.setDictKey(dto.getDictKey());
        item.setDictLabel(dto.getDictLabel());
        item.setSortOrder(dto.getSortOrder() == null ? 0 : dto.getSortOrder());
        item.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        item.setRemark(dto.getRemark());
        dictItemMapper.insert(item);

        // 字典发生变更, 刷新缓存
        refreshCache();
        return item.getId();
    }

    @Override
    public void updateDictItem(Long id, DictItemDTO dto) {
        if (id == null) {
            return;
        }
        DictItem item = dictItemMapper.selectById(id);
        if (item == null) {
            return;
        }
        // 允许更新类型和键, 但通常只会修改名称/排序/状态
        if (dto.getDictType() != null && !dto.getDictType().isBlank()) {
            item.setDictType(dto.getDictType());
        }
        if (dto.getDictKey() != null && !dto.getDictKey().isBlank()) {
            item.setDictKey(dto.getDictKey());
        }
        if (dto.getDictLabel() != null && !dto.getDictLabel().isBlank()) {
            item.setDictLabel(dto.getDictLabel());
        }
        if (dto.getSortOrder() != null) {
            item.setSortOrder(dto.getSortOrder());
        }
        if (dto.getStatus() != null) {
            item.setStatus(dto.getStatus());
        }
        if (dto.getRemark() != null) {
            item.setRemark(dto.getRemark());
        }
        dictItemMapper.updateById(item);

        refreshCache();
    }

    @Override
    public void deleteDictItem(Long id) {
        if (id == null) {
            return;
        }
        
        dictItemMapper.deleteById(id);
        refreshCache();
    }

    @Override
    public Long createPetCategoryAuto(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new BizException(ResultCode.PARAM_ERROR, "类别名称不能为空");
        }
        String trimmedLabel = label.trim();

        // 1. 根据中文名称生成稳定的英文编码（严格依赖 AI, 内部可能抛 BizException）
        String key = generateDictKey(trimmedLabel);

        // 2. 检查是否已存在相同编码
        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictItem::getDictType, "pet_category")
                .eq(DictItem::getDictKey, key)
                .eq(DictItem::getDeleted, 0);
        DictItem existing = dictItemMapper.selectOne(wrapper);
        if (existing != null) {
            log.info("宠物类别编码已存在, dictKey={}, label={}", key, trimmedLabel);
            return existing.getId();
        }

        // 3. 计算排序号
        LambdaQueryWrapper<DictItem> sortWrapper = new LambdaQueryWrapper<>();
        sortWrapper.eq(DictItem::getDictType, "pet_category")
                .eq(DictItem::getDeleted, 0);
        List<DictItem> allItems = dictItemMapper.selectList(sortWrapper);
        int sortOrder = allItems.stream()
                .map(DictItem::getSortOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        // 4. 写入字典表
        DictItem item = new DictItem();
        item.setDictType("pet_category");
        item.setDictKey(key);
        item.setDictLabel(trimmedLabel);
        item.setSortOrder(sortOrder);
        item.setStatus(1);
        dictItemMapper.insert(item);

        // 5. 刷新缓存
        refreshCache();

        log.info("自动创建宠物类别字典项, dictKey={}, label={}", key, trimmedLabel);
        return item.getId();
    }

    /**
     * 生成稳定的英文编码:
     * 1. 尝试对输入进行简单的拉丁化+规范化, 保留字母数字
     * 2. 对纯中文场景, 目前先退化为使用原文的拼音/简写留待后续接入 Qwen
     */
    private String generateDictKey(String label) {
        // 严格依赖 AI 翻译, 不做本地兜底 失败时抛业务异常, 前端显示“新增失败” 
        try {
            String english = translationService.translateToEnglishKey(label);
            if (english == null || english.isBlank()) {
                throw new BizException(ResultCode.AI_REQUEST_FAILED, "AI 翻译返回空结果, 无法生成宠物类别编码");
            }
            return english;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(ResultCode.AI_REQUEST_FAILED, "AI 翻译失败, 无法生成宠物类别编码");
        }
    }

    /**
     * 转换为 DictItemVO
     *
     * @param item
     * @return
     */
    private DictItemVO toDictItemVO(DictItem item) {
        DictItemVO vo = new DictItemVO();

        vo.setId(item.getId());
        vo.setDictType(item.getDictType());
        vo.setDictKey(item.getDictKey());
        vo.setDictLabel(item.getDictLabel());
        vo.setSortOrder(item.getSortOrder());
        vo.setStatus(item.getStatus());
        vo.setRemark(item.getRemark());

        return vo;
    }
}
