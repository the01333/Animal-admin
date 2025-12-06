package com.puxinxiaolin.adopt.entity.dto;

import com.puxinxiaolin.adopt.common.PageInfo;
import lombok.Data;

/**
 * 领养申请分页查询 DTO
 */
@Data
public class AdoptionApplicationPageQueryDTO extends PageInfo {

    /**
     * 申请状态
     */
    private String status;

    /**
     * 关键词（管理员查询用）
     */
    private String keyword;
}
