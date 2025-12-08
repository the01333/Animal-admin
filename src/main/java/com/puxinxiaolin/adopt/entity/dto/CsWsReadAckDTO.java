package com.puxinxiaolin.adopt.entity.dto;

import lombok.Data;

/**
 * WebSocket 人工客服已读回执 DTO
 */
@Data
public class CsWsReadAckDTO {

    /** 会话ID (t_cs_session.id) */
    private Long sessionId;

    /** 已读方: USER / AGENT */
    private String readSide;
}
