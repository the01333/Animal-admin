package com.puxinxiaolin.adopt.entity.dto;

import lombok.Builder;
import lombok.Data;

/**
 * WebSocket 未读汇总推送 DTO
 */
@Data
@Builder
public class CsWsUnreadDTO {

    /** 用户端总未读数 */
    private Integer unreadForUser;

    /** 客服端总未读数 */
    private Integer unreadForAgent;
}
