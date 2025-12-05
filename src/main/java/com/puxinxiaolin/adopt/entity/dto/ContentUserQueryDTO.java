package com.puxinxiaolin.adopt.entity.dto;

import com.puxinxiaolin.adopt.common.PageInfo;
import lombok.Data;

/**
 * 用户内容记录查询 DTO
 */
@Data
public class ContentUserQueryDTO extends PageInfo {

    /**
     * GUIDE 或 STORY，null 表示全部
     */
    private String category;
}
