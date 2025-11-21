package com.animal.adopt.mapper;

import com.animal.adopt.entity.po.ConversationHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 对话历史 Mapper
 */
@Mapper
public interface ConversationHistoryMapper extends BaseMapper<ConversationHistory> {
    
    /**
     * 根据会话ID获取所有对话记录
     */
    @Select("SELECT * FROM t_conversation_history WHERE session_id = #{sessionId} AND deleted = 0 ORDER BY create_time ASC")
    List<ConversationHistory> getBySessionId(String sessionId);
    
    /**
     * 删除会话的所有对话记录
     */
    @Select("DELETE FROM t_conversation_history WHERE session_id = #{sessionId}")
    void deleteBySessionId(String sessionId);
}
