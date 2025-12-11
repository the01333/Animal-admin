package com.puxinxiaolin.adopt.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.entity.dto.ContentQueryDTO;
import com.puxinxiaolin.adopt.entity.dto.PetQueryDTO;
import com.puxinxiaolin.adopt.entity.vo.ContentVO;
import com.puxinxiaolin.adopt.entity.vo.PetVO;
import com.puxinxiaolin.adopt.enums.ContentCategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AiToolService {

    private final PetService petService;
    private final ContentService contentService;

    private static final Map<String, List<String>> KEYWORD_SYNONYMS = Map.of(
            "新手", List.of("新手", "入门", "初学", "基础"),
            "护理", List.of("护理", "照顾", "照护", "保养"),
            "训练", List.of("训练", "教育", "驯养", "行为"),
            "健康", List.of("健康", "医疗", "常见病", "体检"),
            "疫苗", List.of("疫苗", "免疫", "打针"),
            "须知", List.of("须知", "注意事项", "指南")
    );

    private static final List<String> KEYWORD_FALLBACKS = List.of(
            "宠物护理", "领养须知", "养宠技巧", "宠物健康", "领养流程"
    );

    /**
     * 根据条件查询宠物（用于AI工具调用）
     */
    @Tool(description = "根据类别、性格关键词、领养状态查询宠物列表")
    public List<PetVO> searchPets(
            @ToolParam(description = "宠物类别, 如 cat/dog 等", required = false) String category,
            @ToolParam(description = "性格关键词（模糊匹配）", required = false) String personality,
            @ToolParam(description = "领养状态, 如 available/pending/adopted", required = false) String adoptionStatus,
            @ToolParam(description = "返回条数", required = false) Integer limit
    ) {
        PetQueryDTO query = new PetQueryDTO();
        query.setCategory(category);
        query.setAdoptionStatus(adoptionStatus);
        
        // personality 为自由文本, 前端数据结构为 Pet.personality 文本包含
        query.setSize(Long.valueOf(limit));
        Page<PetVO> result = petService.queryPetPage(query);
        
        List<PetVO> records = result.getRecords();
        if (personality != null && !personality.isEmpty()) {
            records = records.stream()
                    .filter(p -> p.getPersonality() != null && p.getPersonality().contains(personality))
                    .toList();
        }
        return records;
    }

    @Tool(description = "根据分类/关键词查询内容列表（指南/故事）")
    public List<ContentVO> searchArticles(
            @ToolParam(description = "文章分类", required = false) String category,
            @ToolParam(description = "关键词(标题/摘要)", required = false) String keyword,
            @ToolParam(description = "状态(0草稿/1发布/2下架)", required = false) Integer status,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        return queryContent(category, keyword, limit);
    }

    @Tool(description = "根据图片URL简单识别动物类别(占位),再查询可领养宠物")
    public List<PetVO> recognizeAndSearchPets(
            @ToolParam(description = "图片URL", required = true) String imageUrl,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        String lower = imageUrl == null ? "" : imageUrl.toLowerCase();
        String category = null;
        if (lower.contains("cat") || lower.contains("mao") || lower.contains("kitten")) category = "cat";
        else if (lower.contains("dog") || lower.contains("gou") || lower.contains("puppy")) category = "dog";
        return searchPets(category, null, "available", limit == null ? 10 : limit);
    }

    @Tool(description = "返回领养须知相关文章摘要")
    public List<ContentVO> adoptionPolicy(
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        return queryContent(ContentCategoryEnum.GUIDE.name(), "须知", limit == null ? 5 : limit);
    }

    /**
     * 根据用户性格特征推荐宠物
     * 用于处理"我性格内向/外向, 适合养什么宠物？"等问题
     */
    @Tool(description = "根据用户性格特征推荐最适合的宠物")
    public List<PetVO> recommendPetsByPersonality(
            @ToolParam(description = "用户性格描述, 如: 内向、外向、忙碌、有耐心等", required = true) String userPersonality,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        // 性格映射到宠物性格
        String petPersonality = mapUserPersonalityToPetPersonality(userPersonality);
        return searchPets(null, petPersonality, "available", limit == null ? 8 : limit);
    }

    /**
     * 根据生活方式推荐宠物
     * 用于处理"我住在公寓/有院子, 适合养什么宠物？"等问题
     */
    @Tool(description = "根据用户生活方式推荐合适的宠物")
    public List<PetVO> recommendPetsByLifestyle(
            @ToolParam(description = "生活方式描述, 如: 公寓、有院子、经常出差、有小孩等", required = true) String lifestyle,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        String personality = mapLifestyleToPetPersonality(lifestyle);
        return searchPets(null, personality, "available", limit == null ? 8 : limit);
    }

    /**
     * 获取宠物护理指南
     * 用于处理"怎样照顾小猫/小狗？"等问题
     */
    @Tool(description = "获取特定宠物类别的护理指南")
    public List<ContentVO> getPetCareGuide(
            @ToolParam(description = "宠物类别, 如: cat/dog/rabbit等", required = true) String petCategory,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        return queryContent(ContentCategoryEnum.GUIDE.name(), petCategory, limit);
    }

    /**
     * 获取新手养宠指南
     * 用于处理"新手养宠要注意什么？"等问题
     */
    @Tool(description = "获取新手养宠的入门指南")
    public List<ContentVO> getBeginnerGuide(
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        return queryContent(ContentCategoryEnum.GUIDE.name(), "新手", limit);
    }

    /**
     * 获取宠物训练指南
     * 用于处理"怎样训练宠物？"等问题
     */
    @Tool(description = "获取宠物训练和教育指南")
    public List<ContentVO> getTrainingGuide(
            @ToolParam(description = "宠物类别或训练主题, 如: dog/cat/行为纠正等", required = false) String topic,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        return queryContent(ContentCategoryEnum.GUIDE.name(), topic, limit);
    }

    /**
     * 获取宠物健康相关信息
     * 用于处理"宠物生病了怎么办？"等问题
     */
    @Tool(description = "获取宠物健康和医疗相关的信息")
    public List<ContentVO> getHealthGuide(
            @ToolParam(description = "健康话题, 如: 疫苗、驱虫、常见病等", required = false) String topic,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        String keyword = topic == null ? "健康" : topic;
        return queryContent(ContentCategoryEnum.GUIDE.name(), keyword, limit);
    }

    /**
     * 获取热门可领养宠物
     * 用于处理"有什么热门的宠物推荐吗？"等问题
     */
    @Tool(description = "获取当前最受欢迎的可领养宠物列表")
    public List<PetVO> getPopularPets(
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        return searchPets(null, null, "available", limit == null ? 10 : limit);
    }

    /**
     * 性格映射: 用户性格 → 宠物性格
     */
    private String mapUserPersonalityToPetPersonality(String userPersonality) {
        if (userPersonality == null) return null;
        
        String lower = userPersonality.toLowerCase();
        
        // 内向的人适合温顺、独立的宠物
        if (lower.contains("内向") || lower.contains("安静") || lower.contains("宅")) {
            return "温顺,独立";
        }
        
        // 外向的人适合活泼、亲人的宠物
        if (lower.contains("外向") || lower.contains("活泼") || lower.contains("社交")) {
            return "活泼,亲人";
        }
        
        // 有耐心的人可以养需要训练的宠物
        if (lower.contains("耐心") || lower.contains("细心")) {
            return "聪慧,需要训练";
        }
        
        // 忙碌的人适合独立的宠物
        if (lower.contains("忙碌") || lower.contains("工作") || lower.contains("出差")) {
            return "独立,自理";
        }
        
        return null;
    }

    /**
     * 生活方式映射: 生活方式 → 宠物性格
     */
    private String mapLifestyleToPetPersonality(String lifestyle) {
        if (lifestyle == null) return null;
        
        String lower = lifestyle.toLowerCase();
        
        // 住公寓的人适合安静、不需要大空间的宠物
        if (lower.contains("公寓") || lower.contains("小房间") || lower.contains("城市")) {
            return "温顺,安静";
        }
        
        // 有院子的人可以养活泼、需要运动的宠物
        if (lower.contains("院子") || lower.contains("别墅") || lower.contains("郊区")) {
            return "活泼,好动";
        }
        
        // 有小孩的家庭需要温顺、耐心的宠物
        if (lower.contains("小孩") || lower.contains("家庭") || lower.contains("孩子")) {
            return "温顺,亲人,耐心";
        }
        
        // 经常出差的人需要独立的宠物
        if (lower.contains("出差") || lower.contains("旅游") || lower.contains("经常外出")) {
            return "独立,自理";
        }
        
        return null;
    }

    private List<ContentVO> queryContent(String category, String keyword, Integer limit) {
        List<String> categoryCandidates = buildCategoryCandidates(category);
        List<String> keywordCandidates = buildKeywordCandidates(keyword);
        long size = limit == null ? 10L : Math.max(limit, 1);

        for (String categoryOption : categoryCandidates) {
            for (String keywordOption : keywordCandidates) {
                ContentQueryDTO queryDTO = new ContentQueryDTO();
                if (StrUtil.isNotBlank(categoryOption)) {
                    queryDTO.setCategory(categoryOption);
                }
                queryDTO.setKeyword(keywordOption);
                queryDTO.setCurrent(1L);
                queryDTO.setSize(size);

                List<ContentVO> records = contentService.queryContentPage(queryDTO).getRecords();
                if (!records.isEmpty()) {
                    return records;
                }
            }
        }

        ContentQueryDTO fallbackQuery = new ContentQueryDTO();
        fallbackQuery.setCurrent(1L);
        fallbackQuery.setSize(size);
        return contentService.queryContentPage(fallbackQuery).getRecords();
    }

    private List<String> buildKeywordCandidates(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return Collections.singletonList(null);
        }

        String normalized = keyword.trim();
        LinkedHashSet<String> candidates = new LinkedHashSet<>();
        candidates.add(normalized);

        KEYWORD_SYNONYMS.forEach((trigger, synonyms) -> {
            if (normalized.contains(trigger)) {
                candidates.addAll(synonyms);
                return;
            }
            for (String synonym : synonyms) {
                if (normalized.contains(synonym)) {
                    candidates.addAll(synonyms);
                    break;
                }
            }
        });

        candidates.addAll(KEYWORD_FALLBACKS);

        return new ArrayList<>(candidates);
    }

    private List<String> buildCategoryCandidates(String category) {
        LinkedHashSet<String> candidates = new LinkedHashSet<>();
        if (StrUtil.isNotBlank(category)) {
            String normalized = category.trim().toUpperCase();
            candidates.add(normalized);
            if (ContentCategoryEnum.GUIDE.name().equalsIgnoreCase(normalized)) {
                candidates.add(ContentCategoryEnum.STORY.name());
            } else if (ContentCategoryEnum.STORY.name().equalsIgnoreCase(normalized)) {
                candidates.add(ContentCategoryEnum.GUIDE.name());
            }
        }
        candidates.add(null);
        return new ArrayList<>(candidates);
    }
}
