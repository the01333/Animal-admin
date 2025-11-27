package com.puxinxiaolin.adopt.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏记录 VO
 */
@Data
public class FavoriteVO {

    private Long id;
    private Long userId;
    private Long petId;
    private LocalDateTime createTime;
}
