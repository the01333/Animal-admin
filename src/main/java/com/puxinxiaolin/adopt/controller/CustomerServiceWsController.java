package com.puxinxiaolin.adopt.controller;

import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.entity.dto.CsSendMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsChatMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsReadAckDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsUnreadDTO;
import com.puxinxiaolin.adopt.entity.entity.ChatMessage;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.ChatMessageMapper;
import com.puxinxiaolin.adopt.service.CustomerServiceLongPollingService;
import com.puxinxiaolin.adopt.service.CustomerServiceSessionService;
import com.puxinxiaolin.adopt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Objects;

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
    private CustomerServiceSessionService customerServiceSessionService;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CustomerServiceLongPollingService customerServiceLongPollingService;
    @Autowired
    private UserService userService;

    /**
     * 处理聊天消息
     */
    @MessageMapping("/cs/chat")
    public void handleChat(@Payload CsWsChatMessageDTO payload, Principal principal) {
        Long currentUserId = resolveCurrentUserId(principal);
        log.info("[WS] 收到聊天消息, sessionId={}, userId={}", payload.getSessionId(), currentUserId);

        if (payload.getSessionId() == null || payload.getSessionId() <= 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "会话ID不能为空");
        }

        CustomerServiceSession session = customerServiceSessionService.getById(payload.getSessionId());
        if (session == null) {
            throw new BizException(ResultCode.NOT_FOUND, "会话不存在");
        }

        boolean isAdmin = isAdminUser(currentUserId);
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

        Long receiverId = null;
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
            int unreadForAgent = session.getUnreadForAgent() == null ? 0 : session.getUnreadForAgent();
            session.setUnreadForAgent(unreadForAgent + 1);
        } else {
            int unreadForUser = session.getUnreadForUser() == null ? 0 : session.getUnreadForUser();
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
    public void handleReadAck(@Payload CsWsReadAckDTO payload, Principal principal) {
        Long currentUserId = resolveCurrentUserId(principal);
        log.info("[WS] 收到已读回执, sessionId={}, readSide={}, userId={}",
                payload.getSessionId(), payload.getReadSide(), currentUserId);

        if (payload.getSessionId() == null || payload.getSessionId() <= 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "会话ID不能为空");
        }

        CustomerServiceSession session = customerServiceSessionService.getById(payload.getSessionId());
        if (session == null) {
            throw new BizException(ResultCode.NOT_FOUND, "会话不存在");
        }

        boolean isAdmin = isAdminUser(currentUserId);
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

    private Long resolveCurrentUserId(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new BizException(ResultCode.UNAUTHORIZED, "WebSocket 会话未登录");
        }
        try {
            return Long.valueOf(principal.getName());
        } catch (NumberFormatException e) {
            throw new BizException(ResultCode.UNAUTHORIZED, "WebSocket 会话用户ID非法");
        }
    }

    private boolean isAdminUser(Long userId) {
        if (userId == null) {
            return false;
        }
        User user = userService.getById(userId);
        if (user == null || user.getRole() == null) {
            return false;
        }
        String role = user.getRole();
        return "admin".equals(role) || "super_admin".equals(role);
    }
}
