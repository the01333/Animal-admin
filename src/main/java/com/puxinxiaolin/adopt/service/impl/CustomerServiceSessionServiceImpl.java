package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceSessionVO;
import com.puxinxiaolin.adopt.mapper.CustomerServiceSessionMapper;
import com.puxinxiaolin.adopt.service.CustomerServiceSessionService;
import com.puxinxiaolin.adopt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 人工客服会话服务实现
 * <br />
 * 负责用户第一次点击人工会话（创建会话）、后台分页看会话列表、计算红点未读总数（用户端/管理员端）
 */
@Slf4j
@Service
public class CustomerServiceSessionServiceImpl extends ServiceImpl<CustomerServiceSessionMapper, CustomerServiceSession> implements CustomerServiceSessionService {

    @Autowired
    private CustomerServiceSessionMapper customerServiceSessionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    /**
     * 获取/创建当前用户的会话
     *
     * @return
     */
    @Override
    public CustomerServiceSessionVO openOrGetCurrentUserSession() {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("获取/创建人工客服会话, userId={}", userId);

        LambdaQueryWrapper<CustomerServiceSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerServiceSession::getUserId, userId)
                .eq(CustomerServiceSession::getStatus, "OPEN")
                .last("LIMIT 1");

        CustomerServiceSession session = this.getOne(wrapper, false);
        if (session == null) {
            session = new CustomerServiceSession();
            session.setUserId(userId);
            session.setStatus("OPEN");
            session.setUnreadForUser(0);
            session.setUnreadForAgent(0);
            // 为新会话分配一个默认客服, 便于后续用户发消息时直接推送未读给该客服
            Long defaultAgentId = resolveDefaultAgentId();
            if (defaultAgentId != null) {
                session.setAgentId(defaultAgentId);
            }
            this.save(session);
        } else if (session.getAgentId() == null) {
            Long defaultAgentId = resolveDefaultAgentId();
            if (defaultAgentId != null) {
                session.setAgentId(defaultAgentId);
                this.updateById(session);
            }
        }

        return buildSessionVO(session);
    }

    /**
     * <p>分页查询会话列表（后台端 - 超级管理员）<p/>
     * <br />
     * 用于加载左侧会话列表以及展示侧边栏红点未读总数
     *
     * @param current 当前页
     * @param size    每页大小
     * @param keyword 按用户昵称/手机号等关键字搜索（预留）
     * @param status  会话状态（OPEN/CLOSED）, 为空则不过滤
     * @return
     */
    @Override
    public Page<CustomerServiceSessionVO> pageSessionsForAdmin(Long current, Long size, String keyword, String status) {
        log.info("分页查询人工客服会话列表, current={}, size={}, keyword={}, status={}", current, size, keyword, status);

        LambdaQueryWrapper<CustomerServiceSession> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(CustomerServiceSession::getStatus, status);
        }
        if (StrUtil.isNotBlank(keyword)) {
            // 按用户账号/昵称模糊搜索, 先查出匹配的用户ID集合
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getNickname, keyword);
            List<User> matchedUsers = userService.list(userWrapper);
            List<Long> userIds = matchedUsers.stream()
                    .map(User::getId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            if (userIds.isEmpty()) {
                Page<CustomerServiceSessionVO> emptyPage = new Page<>(current, size);
                emptyPage.setTotal(0);
                emptyPage.setRecords(Collections.emptyList());
                return emptyPage;
            }

            wrapper.in(CustomerServiceSession::getUserId, userIds);
        }

        wrapper.orderByDesc(CustomerServiceSession::getLastTime);

        Page<CustomerServiceSession> page = this.page(new Page<>(current, size), wrapper);
        List<CustomerServiceSessionVO> voList = page.getRecords().stream()
                // 管理端列表中不展示管理员/超级管理员自己作为"用户"发起的会话
                .map(session -> buildSessionVO(session, true))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Page<CustomerServiceSessionVO> voPage = new Page<>(current, size);
        voPage.setTotal(page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 获取客服未读消息总数
     *
     * @param agentId
     * @return
     */
    @Override
    public Integer sumUnreadForAgent(Long agentId) {
        if (agentId == null) {
            return 0;
        }

        Integer sum = customerServiceSessionMapper.sumUnreadForAgent(agentId);
        return sum == null ? 0 : sum;
    }

    /**
     * 获取用户未读消息总数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer sumUnreadForUser(Long userId) {
        if (userId == null) {
            return 0;
        }
        Integer sum = customerServiceSessionMapper.sumUnreadForUser(userId);
        return sum == null ? 0 : sum;
    }

    /**
     * 获取所有客服未读消息总数
     *
     * @return
     */
    @Override
    public Integer sumUnreadForAllAgents() {
        Integer sum = customerServiceSessionMapper.sumUnreadForAllAgents();
        return sum == null ? 0 : sum;
    }

    private CustomerServiceSessionVO buildSessionVO(CustomerServiceSession session) {
        // 默认不过滤管理员账号, 供前台 openOrGetCurrentUserSession 使用
        return buildSessionVO(session, false);
    }

    /**
     * 构建会话 VO
     *
     * @param session          会话实体
     * @param excludeAdminUser 是否排除 user 角色为 admin/super_admin 的会话
     */
    private CustomerServiceSessionVO buildSessionVO(CustomerServiceSession session, boolean excludeAdminUser) {
        if (session == null) {
            return null;
        }
        User user = null;
        boolean online = false;
        if (session.getUserId() != null) {
            user = userService.getById(session.getUserId());
            // fixme: 不再过滤管理员用户的会话，因为管理员也可以作为普通用户在前台发起对话
            // if (excludeAdminUser && user != null) {
            //     String role = user.getRole();
            //     if ("admin".equals(role) || "super_admin".equals(role)) {
            //         return null;
            //     }
            // }
            try {
                online = simpUserRegistry.getUser(String.valueOf(session.getUserId())) != null;
            } catch (Exception e) {
                log.warn("查询用户在线状态失败, userId={}", session.getUserId(), e);
            }
        }

        CustomerServiceSessionVO.CustomerServiceSessionVOBuilder voBuilder = CustomerServiceSessionVO.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .agentId(session.getAgentId())
                .status(session.getStatus())
                .lastMessage(session.getLastMessage())
                .lastTime(session.getLastTime())
                .unreadForUser(session.getUnreadForUser())
                .unreadForAgent(session.getUnreadForAgent())
                .online(online);
        if (user != null) {
            voBuilder.userUsername(user.getUsername());
            voBuilder.userNickname(user.getNickname());
            voBuilder.userAvatar(user.getAvatar());
        }
        return voBuilder.build();
    }

    /**
     * 获取默认客服，默认是超级管理员（超级管理员只有一个，并且只有超级管理员才能在后台页面进行会话管理）
     *
     * @return
     */
    private Long resolveDefaultAgentId() {
        try {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getStatus, 1)
                    .eq(User::getRole, "super_admin")
                    .orderByAsc(User::getId);

            List<User> candidates = userService.list(wrapper);
            if (candidates == null || candidates.isEmpty()) {
                return null;
            }

            Long fallback = null;
            for (User u : candidates) {
                if (u == null || u.getId() == null) {
                    continue;
                }
                if (fallback == null) {
                    fallback = u.getId();
                }
                if (simpUserRegistry.getUser(String.valueOf(u.getId())) != null) {
                    return u.getId();
                }
            }

            return fallback;
        } catch (Exception e) {
            log.warn("分配默认客服失败", e);
        }
        return null;
    }
    
}
