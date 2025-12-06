package com.puxinxiaolin.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 宠物分类统计 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCategoryStatsVO {

    /**
     * 分类编码（例如：CAT、DOG、RABBIT、BIRD、OTHER）
     */
    private String category;

    /**
     * 数量
     */
    private Long count;
}
