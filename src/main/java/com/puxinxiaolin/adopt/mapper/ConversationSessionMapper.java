package com.puxinxiaolin.adopt.mapper;

import com.puxinxiaolin.adopt.entity.entity.ConversationSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对话会话 Mapper
 */
@Mapper
public interface ConversationSessionMapper extends BaseMapper<ConversationSession> {
    
    /**
     * 根据会话ID获取会话
     */
    ConversationSession getBySessionId(@Param("sessionId") String sessionId);
    
    /**
     * 获取用户的所有会话
     */
    List<ConversationSession> getUserSessions(@Param("userId") Long userId);
    
    /**
     * 获取用户的活跃会话
     */
    ConversationSession getActiveSession(@Param("userId") Long userId);
}
