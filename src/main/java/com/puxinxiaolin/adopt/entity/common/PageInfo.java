package com.puxinxiaolin.adopt.entity.common;

import lombok.Data;

@Data
public class PageInfo {

    /**
     * 当前页
     */
    private Long current = 1L;

    /**
     * 每页大小
     */
    private Long size = 10L;
    
}
