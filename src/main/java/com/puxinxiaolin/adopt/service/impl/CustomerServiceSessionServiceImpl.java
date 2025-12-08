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
import org.springframework.stereotype.Service;

import java.util.List;
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
                .map(this::buildSessionVO)
                .collect(Collectors.toList());

        Page<CustomerServiceSessionVO> voPage = new Page<>(current, size);
        voPage.setTotal(page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    private CustomerServiceSessionVO buildSessionVO(CustomerServiceSession session) {
        if (session == null) {
            return null;
        }
        User user = null;
        if (session.getUserId() != null) {
            user = userService.getById(session.getUserId());
        }
        CustomerServiceSessionVO.CustomerServiceSessionVOBuilder builder = CustomerServiceSessionVO.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .agentId(session.getAgentId())
                .status(session.getStatus())
                .lastMessage(session.getLastMessage())
                .lastTime(session.getLastTime())
                .unreadForUser(session.getUnreadForUser())
                .unreadForAgent(session.getUnreadForAgent());

        if (user != null) {
            builder.userNickname(user.getNickname());
            builder.userAvatar(user.getAvatar());
        }

        return builder.build();
    }
}
