package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.entity.dto.CsSendMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsChatMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsReadAckDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsUnreadDTO;
import com.puxinxiaolin.adopt.entity.entity.ChatMessage;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.ChatMessageMapper;
import com.puxinxiaolin.adopt.service.CustomerServiceLongPollingService;
import com.puxinxiaolin.adopt.service.CustomerServiceSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * 人工客服 WebSocket STOMP 控制器
 *
 * 约定:
 * - 客户端发送:
 *   - /app/cs/chat      -> CsWsChatMessageDTO
 *   - /app/cs/read-ack  -> CsWsReadAckDTO
 * - 服务端推送:
 *   - /user/queue/cs/chat   -> CustomerServiceMessageVO
 *   - /user/queue/cs/unread -> CsWsUnreadDTO
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class CustomerServiceWsController {

    private final CustomerServiceSessionService customerServiceSessionService;
    private final ChatMessageMapper chatMessageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerServiceLongPollingService customerServiceLongPollingService;

    /**
     * 处理聊天消息
     */
    @MessageMapping("/cs/chat")
    public void handleChat(@Payload CsWsChatMessageDTO payload) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("[WS] 收到聊天消息, sessionId={}, userId={}", payload.getSessionId(), currentUserId);

        if (payload.getSessionId() == null || payload.getSessionId() <= 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "会话ID不能为空");
        }

        CustomerServiceSession session = customerServiceSessionService.getById(payload.getSessionId());
        if (session == null) {
            throw new BizException(ResultCode.NOT_FOUND, "会话不存在");
        }

        boolean isAdmin = StpUtil.hasRole("admin") || StpUtil.hasRole("super_admin");
        if (!isAdmin && !Objects.equals(session.getUserId(), currentUserId)) {
            throw new BizException(ResultCode.FORBIDDEN, "无权发送该会话消息");
        }

        CsSendMessageDTO dto = new CsSendMessageDTO();
        dto.setMessageType(payload.getMessageType());
        dto.setContent(payload.getContent());

        String messageType = (dto.getMessageType() == null || dto.getMessageType().isEmpty())
                ? "text" : dto.getMessageType();

        ChatMessage msg = new ChatMessage();
        msg.setSessionId(String.valueOf(session.getId()));
        msg.setMessageType(messageType);
        msg.setContent(dto.getContent());
        msg.setSenderId(currentUserId);

        Long receiverId;
        if (session.getUserId() != null && currentUserId.equals(session.getUserId())) {
            receiverId = session.getAgentId();
        } else {
            receiverId = session.getUserId();
        }
        msg.setReceiverId(receiverId);
        msg.setIsRead(false);

        chatMessageMapper.insert(msg);

        session.setLastMessage(dto.getContent());
        session.setLastTime(java.time.LocalDateTime.now());

        if (session.getUserId() != null && currentUserId.equals(session.getUserId())) {
            Integer unreadForAgent = session.getUnreadForAgent() == null ? 0 : session.getUnreadForAgent();
            session.setUnreadForAgent(unreadForAgent + 1);
        } else {
            Integer unreadForUser = session.getUnreadForUser() == null ? 0 : session.getUnreadForUser();
            session.setUnreadForUser(unreadForUser + 1);
        }

        customerServiceSessionService.updateById(session);

        CustomerServiceMessageVO.CustomerServiceMessageVOBuilder builder = CustomerServiceMessageVO.builder()
                .id(msg.getId())
                .sessionId(session.getId())
                .senderId(msg.getSenderId())
                .receiverId(msg.getReceiverId())
                .contentType(msg.getMessageType())
                .content(msg.getContent())
                .read(false)
                .createTime(msg.getCreateTime());

        String senderRole = "USER";
        if (session.getUserId() != null && msg.getSenderId() != null) {
            if (!msg.getSenderId().equals(session.getUserId())) {
                senderRole = "AGENT";
            }
        }
        builder.senderRole(senderRole);
        CustomerServiceMessageVO vo = builder.build();

        // 同步通知长轮询等待者
        customerServiceLongPollingService.publishNewMessage(session.getId(), vo);

        Long targetUserId = null;
        if (session.getUserId() != null && currentUserId.equals(session.getUserId())) {
            targetUserId = session.getAgentId();
        } else {
            targetUserId = session.getUserId();
        }

        if (targetUserId != null) {
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(targetUserId),
                    "/queue/cs/chat",
                    vo
            );

            CsWsUnreadDTO unreadDTO = CsWsUnreadDTO.builder()
                    .unreadForUser(session.getUnreadForUser())
                    .unreadForAgent(session.getUnreadForAgent())
                    .build();

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(targetUserId),
                    "/queue/cs/unread",
                    unreadDTO
            );
        }
    }

    /**
     * 处理已读回执
     */
    @MessageMapping("/cs/read-ack")
    public void handleReadAck(@Payload CsWsReadAckDTO payload) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("[WS] 收到已读回执, sessionId={}, readSide={}, userId={}",
                payload.getSessionId(), payload.getReadSide(), currentUserId);

        if (payload.getSessionId() == null || payload.getSessionId() <= 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "会话ID不能为空");
        }

        CustomerServiceSession session = customerServiceSessionService.getById(payload.getSessionId());
        if (session == null) {
            throw new BizException(ResultCode.NOT_FOUND, "会话不存在");
        }

        boolean isAdmin = StpUtil.hasRole("admin") || StpUtil.hasRole("super_admin");
        if (!isAdmin && !Objects.equals(session.getUserId(), currentUserId)) {
            throw new BizException(ResultCode.FORBIDDEN, "无权更新该会话未读状态");
        }

        String side = payload.getReadSide() == null ? "" : payload.getReadSide().toUpperCase();
        switch (side) {
            case "USER" -> session.setUnreadForUser(0);
            case "AGENT" -> session.setUnreadForAgent(0);
            default -> throw new BizException(ResultCode.BAD_REQUEST, "readSide 非法");
        }
        customerServiceSessionService.updateById(session);

        // 推送最新未读汇总给当前用户
        CsWsUnreadDTO unreadDTO = CsWsUnreadDTO.builder()
                .unreadForUser(session.getUnreadForUser())
                .unreadForAgent(session.getUnreadForAgent())
                .build();

        messagingTemplate.convertAndSendToUser(
                String.valueOf(currentUserId),
                "/queue/cs/unread",
                unreadDTO
        );
    }
}
