package com.puxinxiaolin.adopt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puxinxiaolin.adopt.entity.dto.CsSendMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsChatMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsReadAckDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsUnreadDTO;
import com.puxinxiaolin.adopt.entity.entity.ChatMessage;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.ChatMessageMapper;
import com.puxinxiaolin.adopt.service.CustomerServiceLongPollingService;
import com.puxinxiaolin.adopt.service.CustomerServiceSessionService;
import com.puxinxiaolin.adopt.service.CustomerServiceWsService;
import com.puxinxiaolin.adopt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 人工客服 WebSocket 相关业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceWsServiceImpl implements CustomerServiceWsService {

    private final CustomerServiceSessionService customerServiceSessionService;
    private final ChatMessageMapper chatMessageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final CustomerServiceLongPollingService customerServiceLongPollingService;
    private final UserService userService;
    private final SimpUserRegistry simpUserRegistry;

    @Override
    public void handleChat(CsWsChatMessageDTO payload, Long currentUserId) {
        log.info("[WS] 收到聊天消息, sessionId={}, userId={}", payload.getSessionId(), currentUserId);

        if (payload.getSessionId() == null || payload.getSessionId() <= 0) {
            throw new BizException(ResultCodeEnum.BAD_REQUEST, "会话 ID 不能为空");
        }

        CustomerServiceSession session = customerServiceSessionService.getById(payload.getSessionId());
        if (session == null) {
            throw new BizException(ResultCodeEnum.NOT_FOUND, "会话不存在");
        }

        boolean isAdmin = isAdminUser(currentUserId);
        if (!isAdmin && !Objects.equals(session.getUserId(), currentUserId)) {
            throw new BizException(ResultCodeEnum.FORBIDDEN, "无权发送该会话消息");
        }

        CsSendMessageDTO dto = new CsSendMessageDTO();
        dto.setMessageType(payload.getMessageType());
        dto.setContent(payload.getContent());

        String messageType = StringUtils.isBlank(dto.getMessageType()) ? "text" : dto.getMessageType();

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

        boolean userSending = session.getUserId() != null && currentUserId.equals(session.getUserId());

        Integer totalUnreadForUser = customerServiceSessionService.sumUnreadForUser(session.getUserId());
        Integer totalUnreadForAgent = customerServiceSessionService.sumUnreadForAllAgents();
        CsWsUnreadDTO unreadDTO = CsWsUnreadDTO.builder()
                .unreadForUser(totalUnreadForUser)
                .unreadForAgent(totalUnreadForAgent)
                .build();

        if (userSending) {
            List<Long> onlineAdminIds = resolveOnlineSuperAdminIds();
            for (Long adminId : onlineAdminIds) {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(adminId),
                        "/queue/cs/chat",
                        vo
                );
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(adminId),
                        "/queue/cs/unread",
                        unreadDTO
                );
            }
        } else {
            Long targetUserId = session.getUserId();
            if (targetUserId != null) {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(targetUserId),
                        "/queue/cs/chat",
                        vo
                );
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(targetUserId),
                        "/queue/cs/unread",
                        unreadDTO
                );
            }
        }
    }

    @Override
    public void handleReadAck(CsWsReadAckDTO payload, Long currentUserId) {
        log.info("[WS] 收到已读回执, sessionId={}, readSide={}, userId={}",
                payload.getSessionId(), payload.getReadSide(), currentUserId);

        if (payload.getSessionId() == null || payload.getSessionId() <= 0) {
            throw new BizException(ResultCodeEnum.BAD_REQUEST, "会话ID不能为空");
        }

        CustomerServiceSession session = customerServiceSessionService.getById(payload.getSessionId());
        if (session == null) {
            throw new BizException(ResultCodeEnum.NOT_FOUND, "会话不存在");
        }

        boolean isAdmin = isAdminUser(currentUserId);
        if (!isAdmin && !Objects.equals(session.getUserId(), currentUserId)) {
            throw new BizException(ResultCodeEnum.FORBIDDEN, "无权更新该会话未读状态");
        }

        String side = payload.getReadSide() == null ? "" : payload.getReadSide().toUpperCase();
        switch (side) {
            case "USER" -> session.setUnreadForUser(0);
            case "AGENT" -> session.setUnreadForAgent(0);
            default -> throw new BizException(ResultCodeEnum.BAD_REQUEST, "readSide 非法");
        }
        customerServiceSessionService.updateById(session);

        Integer totalUnreadForUser = customerServiceSessionService.sumUnreadForUser(session.getUserId());
        Integer totalUnreadForAgent = customerServiceSessionService.sumUnreadForAllAgents();
        CsWsUnreadDTO unreadDTO = CsWsUnreadDTO.builder()
                .unreadForUser(totalUnreadForUser)
                .unreadForAgent(totalUnreadForAgent)
                .build();

        if ("USER".equals(side)) {
            Long pushUserId = session.getUserId();
            if (pushUserId != null) {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(pushUserId),
                        "/queue/cs/unread",
                        unreadDTO
                );
            }
        } else if ("AGENT".equals(side)) {
            List<Long> onlineAdminIds = resolveOnlineSuperAdminIds();
            for (Long adminId : onlineAdminIds) {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(adminId),
                        "/queue/cs/unread",
                        unreadDTO
                );
            }
        }
    }

    private List<Long> resolveOnlineSuperAdminIds() {
        try {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getStatus, 1)
                    .eq(User::getRole, "super_admin")
                    .orderByAsc(User::getId);

            List<User> candidates = userService.list(wrapper);
            if (candidates == null || candidates.isEmpty()) {
                return List.of();
            }

            return candidates.stream()
                    .filter(u -> u != null && u.getId() != null)
                    .filter(u -> simpUserRegistry.getUser(String.valueOf(u.getId())) != null)
                    .map(User::getId)
                    .toList();
        } catch (Exception e) {
            log.warn("解析在线超级管理员列表失败", e);
        }
        return List.of();
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
