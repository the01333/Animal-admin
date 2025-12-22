package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.entity.dto.CsWsChatMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsReadAckDTO;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.service.CustomerServiceWsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * 人工客服 WebSocket STOMP 控制器
 * <p>
 * 约定: <br />
 * - 客户端发送: <br />
 * - /app/cs/chat      -> CsWsChatMessageDTO <br />
 * - /app/cs/read-ack  -> CsWsReadAckDTO
 * <p>
 * - 服务端推送: <br />
 * - /user/queue/cs/chat   -> CustomerServiceMessageVO <br />
 * - /user/queue/cs/unread -> CsWsUnreadDTO
 */
@Slf4j
@Controller
public class CustomerServiceWsController {

    @Autowired
    private CustomerServiceWsService customerServiceWsService;

    /**
     * 处理聊天消息
     */
    @MessageMapping("/cs/chat")
    public void handleChat(@Payload CsWsChatMessageDTO payload, Principal principal) {
        Long currentUserId = resolveCurrentUserId(principal);
        customerServiceWsService.handleChat(payload, currentUserId);
    }

    /**
     * 处理已读回执
     */
    @MessageMapping("/cs/read-ack")
    public void handleReadAck(@Payload CsWsReadAckDTO payload, Principal principal) {
        Long currentUserId = resolveCurrentUserId(principal);
        customerServiceWsService.handleReadAck(payload, currentUserId);
    }
    private Long resolveCurrentUserId(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new BizException(ResultCodeEnum.UNAUTHORIZED, "WebSocket 会话未登录");
        }
        try {
            return Long.valueOf(principal.getName());
        } catch (NumberFormatException e) {
            throw new BizException(ResultCodeEnum.UNAUTHORIZED, "WebSocket 会话用户ID非法");
        }
    }

}
