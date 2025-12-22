package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.dto.CsWsChatMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsReadAckDTO;

/**
 * 人工客服 WebSocket 相关业务 Service
 */
public interface CustomerServiceWsService {

    /**
     * 处理 WebSocket 聊天消息
     *
     * @param payload      前端发送的聊天消息 DTO
     * @param currentUserId 当前登录用户 ID
     */
    void handleChat(CsWsChatMessageDTO payload, Long currentUserId);

    /**
     * 处理 WebSocket 已读回执
     *
     * @param payload      前端发送的已读回执 DTO
     * @param currentUserId 当前登录用户 ID
     */
    void handleReadAck(CsWsReadAckDTO payload, Long currentUserId);
}
