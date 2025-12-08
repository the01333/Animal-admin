package com.puxinxiaolin.adopt.entity.cassandra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Cassandra 对话历史表
 * <p>
 * 表结构: 
 * - session_id: 会话ID（分区键）
 * - timestamp: 消息时间戳（聚类键, 用于排序）
 * - message_id: 消息ID（聚类键, 确保唯一性）
 * - user_id: 用户ID
 * - role: 消息角色（user/assistant）
 * - content: 消息内容
 * - tool_name: 工具名称（可选）
 * - tool_params: 工具参数（可选）
 * - tool_result: 工具结果（可选）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("conversation_history")
public class ConversationHistoryCassandra {

    @PrimaryKey
    private ConversationHistoryKey key;

    @Column("user_id")
    private Long userId;

    @Column("role")
    private String role;

    @Column("content")
    private String content;

    @Column("tool_name")
    private String toolName;

    @Column("tool_params")
    private String toolParams;

    @Column("tool_result")
    private String toolResult;

    @Column("created_at")
    private Instant createdAt;

    /**
     * 复合主键类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @PrimaryKeyClass
    public static class ConversationHistoryKey {

        @PrimaryKeyColumn(name = "session_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private String sessionId;

        @PrimaryKeyColumn(name = "timestamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Instant timestamp;

        @PrimaryKeyColumn(name = "message_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private UUID messageId;
    }
}
