package com.puxinxiaolin.adopt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 人工客服单条消息 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerServiceMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long sessionId;
    private Long senderId;
    private Long receiverId;
    /**
     * USER / AGENT
     */
    private String senderRole;

    /**
     * TEXT / IMAGE 预留
     */
    private String contentType;
    private String content;

    private Boolean read;
    private LocalDateTime readTime;
    private LocalDateTime createTime;
}
