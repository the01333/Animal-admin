package com.puxinxiaolin.adopt.mapper;

import com.puxinxiaolin.adopt.entity.entity.ConversationHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对话历史 Mapper
 */
@Mapper
public interface ConversationHistoryMapper extends BaseMapper<ConversationHistory> {
    
    /**
     * 根据会话ID获取所有对话记录
     */
    List<ConversationHistory> getBySessionId(@Param("sessionId") String sessionId);
    
    /**
     * 删除会话的所有对话记录
     */
    void deleteBySessionId(@Param("sessionId") String sessionId);
}
