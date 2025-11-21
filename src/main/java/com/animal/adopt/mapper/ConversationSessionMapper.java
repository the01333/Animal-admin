package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.ConversationSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 对话会话 Mapper
 */
@Mapper
public interface ConversationSessionMapper extends BaseMapper<ConversationSession> {
    
    /**
     * 根据会话ID获取会话
     */
    @Select("SELECT * FROM t_conversation_session WHERE session_id = #{sessionId} AND deleted = 0")
    ConversationSession getBySessionId(String sessionId);
    
    /**
     * 获取用户的所有会话
     */
    @Select("SELECT * FROM t_conversation_session WHERE user_id = #{userId} AND deleted = 0 ORDER BY update_time DESC")
    List<ConversationSession> getUserSessions(Long userId);
    
    /**
     * 获取用户的活跃会话
     */
    @Select("SELECT * FROM t_conversation_session WHERE user_id = #{userId} AND status = 'active' AND deleted = 0 ORDER BY update_time DESC LIMIT 1")
    ConversationSession getActiveSession(Long userId);
}
