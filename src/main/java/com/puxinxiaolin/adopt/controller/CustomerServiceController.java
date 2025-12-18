package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.entity.dto.CsSendMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsUnreadDTO;
import com.puxinxiaolin.adopt.entity.entity.ChatMessage;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceSessionVO;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.ChatMessageMapper;
import com.puxinxiaolin.adopt.service.CustomerServiceLongPollingService;
import com.puxinxiaolin.adopt.service.CustomerServiceSessionService;
import com.puxinxiaolin.adopt.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 人工客服控制器
 * <p>
 * 前台用户: 打开会话、查看自己的消息
 * 后台客服: 查看会话列表、查看会话消息
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/cs")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceSessionService customerServiceSessionService;
    private final ChatMessageMapper chatMessageMapper;
    private final CustomerServiceLongPollingService customerServiceLongPollingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;
    private final UserService userService;

    /**
     * 前台用户: 获取或创建当前用户的人工客服会话
     */
    @PostMapping("/session/open")
    public Result<CustomerServiceSessionVO> openOrGetSession() {
        CustomerServiceSessionVO sessionVO = customerServiceSessionService.openOrGetCurrentUserSession();
        return Result.success(sessionVO);
    }

    /**
     * 后台客服: 分页查询会话列表（仅 super_admin 可用）
     */
    @GetMapping("/sessions")
    public Result<Page<CustomerServiceSessionVO>> pageSessions(
            @RequestParam(defaultValue = "1") @Min(1) Long current,
            @RequestParam(defaultValue = "10") @Min(1) Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        if (!isSuperAdmin) {
            log.warn("非超级管理员尝试访问客服会话列表, userId={}", currentUserId);
            throw new BizException(ResultCode.FORBIDDEN, "仅超级管理员可查看客服会话");
        }
        Page<CustomerServiceSessionVO> page = customerServiceSessionService.pageSessionsForAdmin(current, size, keyword, status);
        return Result.success(page);
    }

    /**
     * 获取某个会话的消息列表（前台/后台共用, 根据登录身份校验权限）
     */
    @GetMapping("/sessions/{sessionId}/messages")
    public Result<List<CustomerServiceMessageVO>> getSessionMessages(@PathVariable Long sessionId) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("查询会话消息, sessionId={}, currentUserId={}", sessionId, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            return Result.success(List.of());
        }

        // 权限校验: 普通用户只能查看自己的会话; 仅 super_admin 可以查看任何会话
        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        boolean isAdmin = StpUtil.hasRole("admin") || isSuperAdmin;
        if (!isAdmin && !currentUserId.equals(session.getUserId())) {
            // 非管理员且不是会话所属用户, 返回空列表以避免信息泄露
            return Result.success(List.of());
        }
        if (isAdmin && !isSuperAdmin && !currentUserId.equals(session.getUserId())) {
            // admin 角色不允许查看客服会话
            log.warn("非超级管理员尝试查看客服会话消息, sessionId={}, userId={}", sessionId, currentUserId);
            return Result.success(List.of());
        }

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, String.valueOf(session.getId()))
                .orderByAsc(ChatMessage::getCreateTime);

        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        List<CustomerServiceMessageVO> voList = messages.stream()
                .map(msg -> buildMessageVO(session, msg))
                .toList();

        return Result.success(voList);
    }

    /**
     * 长轮询获取某个会话中的新消息 
     */
    @GetMapping("/sessions/{sessionId}/lp-messages")
    public DeferredResult<Result<List<CustomerServiceMessageVO>>> longPollSessionMessages(
            @PathVariable Long sessionId,
            @RequestParam(value = "lastMessageId", required = false) Long lastMessageId
    ) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("长轮询会话消息, sessionId={}, lastMessageId={}, currentUserId={}",
                sessionId, lastMessageId, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>();
            deferred.setResult(Result.success(List.of()));
            return deferred;
        }

        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        boolean isAdmin = StpUtil.hasRole("admin") || isSuperAdmin;
        if (!isAdmin && !currentUserId.equals(session.getUserId())) {
            DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>();
            deferred.setResult(Result.success(List.of()));
            return deferred;
        }
        if (isAdmin && !isSuperAdmin && !currentUserId.equals(session.getUserId())) {
            DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>();
            deferred.setResult(Result.success(List.of()));
            return deferred;
        }

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, String.valueOf(session.getId()));
        if (lastMessageId != null && lastMessageId > 0) {
            wrapper.gt(ChatMessage::getId, lastMessageId);
        }
        wrapper.orderByAsc(ChatMessage::getCreateTime);

        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        if (!messages.isEmpty()) {
            List<CustomerServiceMessageVO> voList = messages.stream()
                    .map(msg -> buildMessageVO(session, msg))
                    .collect(Collectors.toList());
            DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>();
            deferred.setResult(Result.success(voList));
            return deferred;
        }

        // 没有新消息时, 注册长轮询等待者
        return customerServiceLongPollingService.registerSessionWaiter(sessionId);
    }

    /**
     * 发送消息（前台用户或后台客服均可使用）
     */
    @PostMapping("/sessions/{sessionId}/messages")
    public Result<CustomerServiceMessageVO> sendMessage(
            @PathVariable Long sessionId,
            @Valid @RequestBody CsSendMessageDTO body
    ) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("发送人工客服消息, sessionId={}, currentUserId={}", sessionId, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            throw new BizException(ResultCode.NOT_FOUND, "会话不存在");
        }

        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        boolean isAdmin = StpUtil.hasRole("admin") || isSuperAdmin;
        if (!isAdmin && !currentUserId.equals(session.getUserId())) {
            throw new BizException(ResultCode.FORBIDDEN, "无权发送该会话消息");
        }
        if (isAdmin && !isSuperAdmin) {
            // 普通 admin 不允许作为客服发送消息
            throw new BizException(ResultCode.FORBIDDEN, "仅超级管理员可作为客服对话");
        }

        String messageType = StringUtils.isBlank(body.getMessageType()) ? "text" : body.getMessageType();

        // 构造消息实体
        ChatMessage msg = new ChatMessage();
        msg.setSessionId(String.valueOf(session.getId()));
        msg.setMessageType(messageType);
        msg.setContent(body.getContent());
        msg.setSenderId(currentUserId);

        Long receiverId = null;
        boolean userSending = session.getUserId() != null && currentUserId.equals(session.getUserId());
        if (userSending) {
            receiverId = session.getAgentId();
            if (receiverId == null || simpUserRegistry.getUser(String.valueOf(receiverId)) == null) {
                Long onlineAgentId = resolveFirstOnlineSuperAdminId();
                if (onlineAgentId != null) {
                    receiverId = onlineAgentId;
                    session.setAgentId(onlineAgentId);
                }
            }
        } else {
            receiverId = session.getUserId();
            if (isSuperAdmin && (session.getAgentId() == null || !currentUserId.equals(session.getAgentId()))) {
                session.setAgentId(currentUserId);
            }
        }
        msg.setReceiverId(receiverId);
        msg.setIsRead(false);

        chatMessageMapper.insert(msg);

        // 更新会话的最后消息 & 未读数
        session.setLastMessage(body.getContent());
        session.setLastTime(LocalDateTime.now());

        if (session.getUserId() != null && currentUserId.equals(session.getUserId())) {
            // 用户发消息 -> 客服未读数 +1
            int unreadForAgent = session.getUnreadForAgent() == null ? 0 : session.getUnreadForAgent();
            session.setUnreadForAgent(unreadForAgent + 1);
        } else {
            // 客服发消息 -> 用户未读数 +1
            int unreadForUser = session.getUnreadForUser() == null ? 0 : session.getUnreadForUser();
            session.setUnreadForUser(unreadForUser + 1);
        }
        customerServiceSessionService.updateById(session);

        // 构建返回 VO
        CustomerServiceMessageVO vo = buildMessageVO(session, msg);

        // 通知所有长轮询等待者有新消息
        customerServiceLongPollingService.publishNewMessage(session.getId(), vo);

        // 通过 WebSocket 推送最新未读汇总给接收方 (用户或客服), 用于驱动前台/后台入口红点
        if (receiverId != null) {
            Integer totalUnreadForUser = customerServiceSessionService.sumUnreadForUser(session.getUserId());
            Integer totalUnreadForAgent = customerServiceSessionService.sumUnreadForAllAgents();
            log.info("[WS] 准备推送未读汇总, receiverId={}, unreadForUser={}, unreadForAgent={}",
                    receiverId, totalUnreadForUser, totalUnreadForAgent);

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
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(receiverId),
                        "/queue/cs/chat",
                        vo
                );
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(receiverId),
                        "/queue/cs/unread",
                        unreadDTO
                );
            }

            // 打印当前在线的 WebSocket 用户及其订阅, 便于排查 convertAndSendToUser 是否能命中
            for (SimpUser user : simpUserRegistry.getUsers()) {
                log.info("[WS] 在线用户: name={}, sessionCount={}", user.getName(), user.getSessions().size());
                for (SimpSession simpSession : user.getSessions()) {
                    for (SimpSubscription sub : simpSession.getSubscriptions()) {
                        log.info("[WS]   sessionId={}, destination={}", simpSession.getId(), sub.getDestination());
                    }
                }
            }
        }

        return Result.success(vo);
    }

    /**
     * 已读回执: 将会话的未读数清零
     */
    @PostMapping("/sessions/{sessionId}/read-ack")
    public Result<Void> readAck(
            @PathVariable Long sessionId,
            @RequestParam("readSide") String readSide
    ) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("更新会话未读状态, sessionId={}, readSide={}, userId={}", sessionId, readSide, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            throw new BizException(ResultCode.NOT_FOUND, "会话不存在");
        }

        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        boolean isAdmin = StpUtil.hasRole("admin") || isSuperAdmin;
        if (!isAdmin && !currentUserId.equals(session.getUserId())) {
            throw new BizException(ResultCode.FORBIDDEN, "无权更新该会话未读状态");
        }
        if (isAdmin && !isSuperAdmin && !currentUserId.equals(session.getUserId())) {
            throw new BizException(ResultCode.FORBIDDEN, "仅超级管理员可更新客服会话未读状态");
        }

        String side = readSide == null ? "" : readSide.toUpperCase();
        switch (side) {
            case "USER" -> session.setUnreadForUser(0);
            case "AGENT" -> {
                if (!isSuperAdmin) {
                    throw new BizException(ResultCode.FORBIDDEN, "仅超级管理员可更新客服侧未读状态");
                }
                session.setAgentId(currentUserId);
                session.setUnreadForAgent(0);
            }
            default -> throw new BizException(ResultCode.BAD_REQUEST, "readSide 非法");
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
        return Result.success(null);
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

    private CustomerServiceMessageVO buildMessageVO(CustomerServiceSession session, ChatMessage msg) {
        CustomerServiceMessageVO.CustomerServiceMessageVOBuilder builder = CustomerServiceMessageVO.builder()
                .id(msg.getId())
                .sessionId(session.getId())
                .senderId(msg.getSenderId())
                .receiverId(msg.getReceiverId())
                .contentType(msg.getMessageType())
                .content(msg.getContent())
                .read(Boolean.TRUE.equals(msg.getIsRead()))
                .readTime(msg.getReadTime())
                .createTime(msg.getCreateTime());

        String senderRole = "USER";
        if (session.getUserId() != null && msg.getSenderId() != null) {
            if (!msg.getSenderId().equals(session.getUserId())) {
                senderRole = "AGENT";
            }
        }
        builder.senderRole(senderRole);
        return builder.build();
    }

    private Long resolveFirstOnlineSuperAdminId() {
        try {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getStatus, 1)
                    .eq(User::getRole, "super_admin")
                    .orderByAsc(User::getId);

            List<User> candidates = userService.list(wrapper);
            if (candidates == null || candidates.isEmpty()) {
                return null;
            }

            for (User u : candidates) {
                if (u == null || u.getId() == null) {
                    continue;
                }
                if (simpUserRegistry.getUser(String.valueOf(u.getId())) != null) {
                    return u.getId();
                }
            }
        } catch (Exception e) {
            log.warn("解析在线超级管理员失败", e);
        }
        return null;
    }
}
