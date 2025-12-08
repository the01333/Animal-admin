package com.puxinxiaolin.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 人工客服会话 VO
 * 用于前后台展示会话列表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerServiceSessionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /** 用户信息 */
    private Long userId;
    private String userNickname;
    private String userAvatar;

    /** 客服信息 */
    private Long agentId;
    private String agentNickname;

    /** 会话状态: OPEN / CLOSED */
    private String status;

    /** 最近一条消息内容及时间 */
    private String lastMessage;
    private LocalDateTime lastTime;

    /** 未读数 */
    private Integer unreadForUser;
    private Integer unreadForAgent;
}
