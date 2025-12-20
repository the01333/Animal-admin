package com.puxinxiaolin.adopt.entity.dto;

import com.puxinxiaolin.adopt.entity.common.PageInfo;
import lombok.Data;

/**
 * 内容查询条件
 */
@Data
public class ContentQueryDTO extends PageInfo {
    
    /**
     * GUIDE or STORY, null 表示全部
     */
    private String category;
    
    private String keyword;

}
