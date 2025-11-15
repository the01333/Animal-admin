package com.animal.adopt.common;

import lombok.Data;

@Data
public class PageInfo {

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;
    
}
