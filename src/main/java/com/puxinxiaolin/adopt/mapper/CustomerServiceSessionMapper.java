package com.puxinxiaolin.adopt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.puxinxiaolin.adopt.entity.entity.CustomerServiceSession;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人工客服会话 Mapper
 */
@Mapper
public interface CustomerServiceSessionMapper extends BaseMapper<CustomerServiceSession> {

    @Select("SELECT COALESCE(SUM(unread_for_agent), 0) FROM t_cs_session WHERE deleted = 0 AND status = 'OPEN' AND agent_id = #{agentId}")
    Integer sumUnreadForAgent(@Param("agentId") Long agentId);

    @Select("SELECT COALESCE(SUM(unread_for_agent), 0) FROM t_cs_session WHERE deleted = 0 AND status = 'OPEN'")
    Integer sumUnreadForAllAgents();

    @Select("SELECT COALESCE(SUM(unread_for_user), 0) FROM t_cs_session WHERE deleted = 0 AND status = 'OPEN' AND user_id = #{userId}")
    Integer sumUnreadForUser(@Param("userId") Long userId);
}
