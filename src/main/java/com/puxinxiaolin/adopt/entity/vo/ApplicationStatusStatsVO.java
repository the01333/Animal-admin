package com.puxinxiaolin.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 申请状态统计 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusStatsVO {

    /**
     * 状态编码（例如：PENDING、APPROVED、REJECTED、CANCELLED）
     */
    private String status;

    /**
     * 数量
     */
    private Long count;
}
