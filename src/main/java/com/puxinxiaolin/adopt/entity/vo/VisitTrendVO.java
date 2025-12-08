package com.puxinxiaolin.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 访问趋势 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitTrendVO {

    /**
     * 日期（格式: yyyy-MM-dd）
     */
    private String date;

    /**
     * 数量
     */
    private Long count;
}
