package com.animal.adopt.service;

import com.animal.adopt.entity.dto.PetQueryDTO;
import com.animal.adopt.entity.po.Article;
import com.animal.adopt.entity.po.Pet;
import com.animal.adopt.entity.vo.PetVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiToolService {

    private final com.animal.adopt.service.PetService petService;
    private final ArticleService articleService;

    /**
     * 根据条件查询宠物（用于AI工具调用）
     */
    @Tool(description = "根据类别、性格关键词、领养状态查询宠物列表")
    public List<PetVO> searchPets(
            @ToolParam(description = "宠物类别，如 cat/dog 等", required = false) String category,
            @ToolParam(description = "性格关键词（模糊匹配）", required = false) String personality,
            @ToolParam(description = "领养状态，如 available/pending/adopted", required = false) String adoptionStatus,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        PetQueryDTO query = new PetQueryDTO();
        query.setCategory(category);
        query.setAdoptionStatus(adoptionStatus);
        
        // personality 为自由文本，前端数据结构为 Pet.personality 文本包含
        Page<Pet> page = new Page<>(1, limit == null ? 10 : limit);
        Page<PetVO> result = petService.queryPetPage(page, query);
        
        List<PetVO> records = result.getRecords();
        if (personality != null && !personality.isEmpty()) {
            records = records.stream()
                    .filter(p -> p.getPersonality() != null && p.getPersonality().contains(personality))
                    .toList();
        }
        return records;
    }

    @Tool(description = "根据分类/关键词查询文章列表")
    public java.util.List<Article> searchArticles(
            @ToolParam(description = "文章分类", required = false) String category,
            @ToolParam(description = "关键词(标题/摘要)", required = false) String keyword,
            @ToolParam(description = "状态(0草稿/1发布/2下架)", required = false) Integer status,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Article> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, limit == null ? 10 : limit);
        return articleService.queryArticlePage(page, category, status, keyword).getRecords();
    }

    @Tool(description = "根据图片URL简单识别动物类别(占位),再查询可领养宠物")
    public java.util.List<PetVO> recognizeAndSearchPets(
            @ToolParam(description = "图片URL", required = true) String imageUrl,
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        String lower = imageUrl == null ? "" : imageUrl.toLowerCase();
        String category = null;
        if (lower.contains("cat") || lower.contains("mao") || lower.contains("kitten")) category = "cat";
        else if (lower.contains("dog") || lower.contains("gou") || lower.contains("puppy")) category = "dog";
        return searchPets(category, null, "available", limit == null ? 10 : limit);
    }

    @Tool(description = "返回领养须知相关文章摘要")
    public java.util.List<Article> adoptionPolicy(
            @ToolParam(description = "返回条数", required = false) Integer limit) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Article> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, limit == null ? 5 : limit);
        return articleService.queryArticlePage(page, "policy", 1, null).getRecords();
    }
}
