package com.puxinxiaolin.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计数据DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsVO {
    
    /**
     * 宠物总数
     */
    private Long totalPets;
    
    /**
     * 可领养宠物数
     */
    private Long availablePets;
    
    /**
     * 已领养宠物数
     */
    private Long adoptedPets;
    
    /**
     * 用户总数
     */
    private Long totalUsers;
    
    /**
     * 待审核申请数
     */
    private Long pendingApplications;
}
