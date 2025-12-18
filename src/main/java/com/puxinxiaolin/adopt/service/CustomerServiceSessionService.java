package com.puxinxiaolin.adopt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import com.puxinxiaolin.adopt.entity.vo.CustomerServiceSessionVO;

/**
 * 人工客服会话服务
 */
public interface CustomerServiceSessionService extends IService<CustomerServiceSession> {

    /**
     * 为当前登录用户获取或创建一个 OPEN 状态的会话
     */
    CustomerServiceSessionVO openOrGetCurrentUserSession();

    /**
     * 管理员/客服分页查询会话列表
     *
     * @param current 当前页
     * @param size    每页大小
     * @param keyword 按用户昵称/手机号等关键字搜索（预留）
     * @param status  会话状态（OPEN/CLOSED）, 为空则不过滤
     */
    Page<CustomerServiceSessionVO> pageSessionsForAdmin(Long current, Long size, String keyword, String status);

    Integer sumUnreadForAgent(Long agentId);

    Integer sumUnreadForAllAgents();

    Integer sumUnreadForUser(Long userId);
}
