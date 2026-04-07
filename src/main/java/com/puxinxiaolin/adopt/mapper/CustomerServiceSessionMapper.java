package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 人工客服会话 Mapper
 */
@Mapper
public interface CustomerServiceSessionMapper extends BaseMapper<CustomerServiceSession> {
    
    /**
     * 统计所有客服未读消息数
     */
    Integer sumUnreadForAllAgents();

    /**
     * 统计用户未读消息数
     */
    Integer sumUnreadForUser(@Param("userId") Long userId);
}
