package com.animal.adopt.repository;

import com.animal.adopt.entity.cassandra.ConversationHistoryCassandra;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Cassandra 对话历史 Repository
 */
@Repository
public interface ConversationHistoryCassandraRepository 
        extends CassandraRepository<ConversationHistoryCassandra, ConversationHistoryCassandra.ConversationHistoryKey> {

    /**
     * 根据会话ID查询对话历史（按时间倒序）
     */
    @Query("SELECT * FROM conversation_history WHERE session_id = ?0 ORDER BY timestamp DESC")
    List<ConversationHistoryCassandra> findBySessionId(String sessionId);

    /**
     * 根据会话ID和用户ID查询对话历史
     */
    @Query("SELECT * FROM conversation_history WHERE session_id = ?0 AND user_id = ?1 ALLOW FILTERING")
    List<ConversationHistoryCassandra> findBySessionIdAndUserId(String sessionId, Long userId);

    /**
     * 根据会话ID查询最近N条消息
     */
    @Query("SELECT * FROM conversation_history WHERE session_id = ?0 ORDER BY timestamp DESC LIMIT ?1")
    List<ConversationHistoryCassandra> findRecentMessages(String sessionId, int limit);

    /**
     * 删除会话的所有消息
     */
    @Query("DELETE FROM conversation_history WHERE session_id = ?0")
    void deleteBySessionId(String sessionId);
}
