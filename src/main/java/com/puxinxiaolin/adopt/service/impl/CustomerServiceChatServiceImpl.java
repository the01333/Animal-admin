package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.puxinxiaolin.adopt.entity.common.Result;
import com.puxinxiaolin.adopt.entity.dto.CsSendMessageDTO;
import com.puxinxiaolin.adopt.entity.dto.CsWsUnreadDTO;
import com.puxinxiaolin.adopt.entity.entity.ChatMessage;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceMessageVO;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.ChatMessageMapper;
import com.puxinxiaolin.adopt.service.CustomerServiceChatService;
import com.puxinxiaolin.adopt.service.CustomerServiceLongPollingService;
import com.puxinxiaolin.adopt.service.CustomerServiceSessionService;
import com.puxinxiaolin.adopt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>人工客服消息（HTTP）相关业务实现<p/>
 * <br />
 * 场景：发送消息 <br />
 * 1. 获取会话（不存在先创建） <br />
 * 2. 获取用户身份并校验 <br />
 * 3. 构建消息实体并根据用户身份获取消息接收者 <br />
 * 4. 保存消息到 DB 的消息历史表中 <br />
 * 5. 更新 DB 会话表的未读数（管理员未读数 + 1） <br />
 * 6. 唤醒 http 长轮询（无论 http 还是 ws 发送消息都必须做，ws 做是为了兜底）<br />
 * 7. ws 推送未读数和消息 - 给所有在线的管理员广播消息和未读数更新 <br />
 * <br />
 * <p>
 * 负责走 HTTP 的消息业务，即使无 ws，也能让前端通过 HTTP 完成聊天（长轮询兜底）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceChatServiceImpl implements CustomerServiceChatService {

    private final CustomerServiceSessionService customerServiceSessionService;
    private final CustomerServiceLongPollingService customerServiceLongPollingService;
    private final ChatMessageMapper chatMessageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    // 在线用户注册中心 - 查询在线状态
    private final SimpUserRegistry simpUserRegistry;
    private final UserService userService;

    /**
     * 获取会话历史消息
     *
     * @param sessionId
     * @return
     */
    @Override
    public List<CustomerServiceMessageVO> getSessionMessages(Long sessionId) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("查询会话消息, sessionId={}, currentUserId={}", sessionId, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            return List.of();
        }

        // 权限校验: 
        // 1. 会话所有者（用户）可以查看自己的会话
        // 2. 超级管理员可以查看任何会话
        // 3. 其他情况不允许
        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        boolean isSessionOwner = currentUserId.equals(session.getUserId());

        if (!isSessionOwner && !isSuperAdmin) {
            // 既不是会话所有者，也不是超级管理员，返回空列表
            return List.of();
        }

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, String.valueOf(session.getId()))
                .orderByAsc(ChatMessage::getCreateTime);

        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        return messages.stream()
                .map(msg -> buildMessageVO(session, msg))
                .toList();
    }

    /**
     * 长轮询获取新消息
     *
     * @param sessionId
     * @param lastMessageId 前端记录的最新的消息 ID
     * @return
     */
    @Override
    public DeferredResult<Result<List<CustomerServiceMessageVO>>> longPollSessionMessages(Long sessionId, Long lastMessageId) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("长轮询会话消息, sessionId={}, lastMessageId={}, currentUserId={}",
                sessionId, lastMessageId, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            /**
             * DeferredResult - Spring 提供的异步结果容器
             * <br />
             * 作用：快速创建一个异步返回对象，并立即设置 “成功 + 空列表” 的结果返回给客户端，本质是返回一个 “无消息” 的成功响应；
             * 原理：允许请求处理线程先返回，释放容器线程资源，后续在其他线程（比如 WebSocket 消息接收线程）中完成结果设置并响应客户端
             * <br />
             * 常见的触发场景：
             * 1）客户刚进入对话页面，前端发起请求获取历史消息，但此时暂无任何消息（比如新对话）；
             * 2）WebSocket 连接建立后，后端需要先返回一个 “兜底响应”，避免客户端长时间等待；
             * 3）异步处理的兜底逻辑：如果没有查到任何消息，直接返回空列表的成功结果，而非阻塞或报错
             */
            DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>();
            deferred.setResult(Result.success(List.of()));
            return deferred;
        }

        // 权限校验: 
        // 1. 会话所有者（用户）可以长轮询自己的会话
        // 2. 超级管理员可以长轮询任何会话
        // 3. 其他情况不允许
        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        boolean isSessionOwner = currentUserId.equals(session.getUserId());
        if (!isSessionOwner && !isSuperAdmin) {
            DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>();
            deferred.setResult(Result.success(List.of()));
            return deferred;
        }

        // 查询“比最后一条消息ID新”的消息（也就是检测是否有新消息）
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, String.valueOf(session.getId()));
        if (lastMessageId != null && lastMessageId > 0) {
            // 看是否存在比 lastMessageId 更大的消息ID，有则说明有新消息
            wrapper.gt(ChatMessage::getId, lastMessageId);
        }
        wrapper.orderByAsc(ChatMessage::getCreateTime);

        List<ChatMessage> messages = chatMessageMapper.selectList(wrapper);
        // 有新消息 → 立刻返回新消息（通过 Spring 提供的异步容器传递）
        if (CollUtil.isNotEmpty(messages)) {
            List<CustomerServiceMessageVO> voList = messages.stream()
                    .map(msg -> buildMessageVO(session, msg))
                    .collect(Collectors.toList());
            DeferredResult<Result<List<CustomerServiceMessageVO>>> deferred = new DeferredResult<>();
            deferred.setResult(Result.success(voList));
            return deferred;
        }

        // 没有新消息时, 注册长轮询等待者（先不返回结果，等有新消息了再返回并通知前端）
        return customerServiceLongPollingService.registerSessionWaiter(sessionId);
    }

    /**
     * 发送消息
     *
     * @param sessionId
     * @param body
     * @return
     */
    @Override
    public CustomerServiceMessageVO sendMessage(Long sessionId, CsSendMessageDTO body) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("发送人工客服消息, sessionId={}, currentUserId={}", sessionId, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            throw new BizException(ResultCodeEnum.NOT_FOUND, "会话不存在");
        }

        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
//        boolean isAdmin = StpUtil.hasRole("admin") || isSuperAdmin;

        // 判断是否是用户在发送消息（基于会话所有权，而不是角色）
        boolean userSending = session.getUserId() != null && currentUserId.equals(session.getUserId());

        // 权限检查：
        // 1. 如果是用户发送（会话所有者），允许
        // 2. 如果是超级管理员作为客服发送，允许
        // 3. 其他情况拒绝
        if (!userSending && !isSuperAdmin) {
            throw new BizException(ResultCodeEnum.FORBIDDEN, "仅超级管理员可作为客服对话");
        }

        String messageType = StringUtils.isBlank(body.getMessageType()) ? "text" : body.getMessageType();

        // 构造消息实体
        ChatMessage msg = new ChatMessage();
        msg.setSessionId(String.valueOf(session.getId()));
        msg.setMessageType(messageType);
        msg.setContent(body.getContent());
        msg.setSenderId(currentUserId);

        Long receiverId;
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

        // 更新会话的未读数量
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

        // 通知所有长轮询等待者有新消息（唤醒长轮询）
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

            // ws 连接建立后，向指定路径（在 ws 配置文件中已定义）推送消息（消息和未读数）
            if (userSending) {
                List<Long> onlineAdminIds = resolveOnlineSuperAdminIds();
                log.info("[WS] userSending=true, sessionId={}, senderUserId={}, onlineAdminIds={}",
                        session.getId(), currentUserId, onlineAdminIds);
                for (Long adminId : onlineAdminIds) {
                    log.info("[WS] 即将推送给管理员, adminId={}, simpUserExists={}",
                            adminId, simpUserRegistry.getUser(String.valueOf(adminId)) != null);
                }
                for (Long adminId : onlineAdminIds) {
                    // 推送具体的聊天消息给管理员
                    messagingTemplate.convertAndSendToUser(
                            String.valueOf(adminId),
                            "/queue/cs/chat",
                            vo
                    );

                    // 推送未读数更新给管理员
                    messagingTemplate.convertAndSendToUser(
                            String.valueOf(adminId),
                            "/queue/cs/unread",
                            unreadDTO
                    );
                    log.info("[WS] 已推送给管理员, adminId={}, sessionId={}, msgId={}",
                            adminId, session.getId(), msg.getId());
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

            // 打印当前在线的 ws 用户及其订阅, 这里我用于排查 convertAndSendToUser 点对点是否能命中
            for (SimpUser user : simpUserRegistry.getUsers()) {
                log.info("[WS] 在线用户: name={}, sessionCount={}", user.getName(), user.getSessions().size());
                for (SimpSession simpSession : user.getSessions()) {
                    for (SimpSubscription sub : simpSession.getSubscriptions()) {
                        log.info("[WS]   sessionId={}, destination={}", simpSession.getId(), sub.getDestination());
                    }
                }
            }
        }
        return vo;
    }

    /**
     * 消息已读回执
     *
     * @param sessionId
     * @param readSide
     */
    @Override
    public void readAck(Long sessionId, String readSide) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        log.info("更新会话未读状态, sessionId={}, readSide={}, userId={}", sessionId, readSide, currentUserId);

        CustomerServiceSession session = customerServiceSessionService.getById(sessionId);
        if (session == null) {
            throw new BizException(ResultCodeEnum.NOT_FOUND, "会话不存在");
        }

        boolean isSuperAdmin = StpUtil.hasRole("super_admin");
        boolean isSessionOwner = currentUserId.equals(session.getUserId());

        // 权限检查：
        // 1. 会话所有者（用户）可以标记自己的消息为已读
        // 2. 超级管理员可以标记任何会话的消息为已读
        // 3. 其他情况不允许
        if (!isSessionOwner && !isSuperAdmin) {
            throw new BizException(ResultCodeEnum.FORBIDDEN, "无权更新该会话未读状态");
        }

        String side = readSide == null ? "" : readSide.toUpperCase();
        // 根据标记的“已读方”清空未读数
        switch (side) {
            case "USER" -> {
                // 只有会话所有者可以标记用户侧已读
                if (!isSessionOwner) {
                    throw new BizException(ResultCodeEnum.FORBIDDEN, "只有用户本人可以标记用户侧消息为已读");
                }
                session.setUnreadForUser(0);
            }
            case "AGENT" -> {
                // 只有超级管理员可以标记客服侧已读
                if (!isSuperAdmin) {
                    throw new BizException(ResultCodeEnum.FORBIDDEN, "仅超级管理员可更新客服侧未读状态");
                }
                // 注释掉：允许所有管理员更新客服侧未读状态
                // if (!isSuperAdmin) {
                //     throw new BizException(ResultCodeEnum.FORBIDDEN, "仅超级管理员可更新客服侧未读状态");
                // }
                session.setAgentId(currentUserId);
                session.setUnreadForAgent(0);
            }
            default -> throw new BizException(ResultCodeEnum.BAD_REQUEST, "readSide 非法");
        }

        customerServiceSessionService.updateById(session);

        Integer totalUnreadForUser = customerServiceSessionService.sumUnreadForUser(session.getUserId());
        Integer totalUnreadForAgent = customerServiceSessionService.sumUnreadForAllAgents();

        CsWsUnreadDTO unreadDTO = CsWsUnreadDTO.builder()
                .unreadForUser(totalUnreadForUser)
                .unreadForAgent(totalUnreadForAgent)
                .build();

        // ws 连接建立后，向指定路径（在 ws 配置文件中已定义）推送未读数
        if ("USER".equals(side)) {
            Long pushUserId = session.getUserId();
            if (pushUserId != null) {
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(pushUserId),
                        "/queue/cs/unread",
                        unreadDTO
                );
            }
        } else {
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

    /**
     * 获取在线的第一个超级管理员 ID - 系统后面只支持一个超级管理员，直接获取也行，无需使用该方法
     *
     * @return
     */
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
