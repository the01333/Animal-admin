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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 人工客服会话服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceSessionServiceImpl extends ServiceImpl<CustomerServiceSessionMapper, CustomerServiceSession>
        implements CustomerServiceSessionService {

    private final UserService userService;
    private final SimpUserRegistry simpUserRegistry;

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
            this.save(session);
        }

        return buildSessionVO(session);
    }

    @Override
    public Page<CustomerServiceSessionVO> pageSessionsForAdmin(Long current, Long size, String keyword, String status) {
        log.info("分页查询人工客服会话列表, current={}, size={}, keyword={}, status={}", current, size, keyword, status);

        LambdaQueryWrapper<CustomerServiceSession> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(CustomerServiceSession::getStatus, status);
        }
        // 预留关键字搜索, 后续可以通过 join 或在上层补充
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
            if (excludeAdminUser && user != null) {
                String role = user.getRole();
                if ("admin".equals(role) || "super_admin".equals(role)) {
                    return null;
                }
            }
            try {
                online = simpUserRegistry.getUser(String.valueOf(session.getUserId())) != null;
            } catch (Exception e) {
                log.warn("查询用户在线状态失败, userId={}", session.getUserId(), e);
            }
        }

        CustomerServiceSessionVO.CustomerServiceSessionVOBuilder builder = CustomerServiceSessionVO.builder()
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
            builder.userNickname(user.getNickname());
            builder.userAvatar(user.getAvatar());
        }
        return builder.build();
    }
}
