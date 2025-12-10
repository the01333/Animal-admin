package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 人工客服会话实体
 * 用于前台用户与后台客服之间的会话管理
 */
@Data
@TableName("t_cs_session")
public class CustomerServiceSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（普通用户）
     */
    private Long userId;

    /**
     * 客服ID（管理员账号, 可为空表示未分配）
     */
    private Long agentId;

    /**
     * 会话状态: OPEN / CLOSED
     */
    private String status;

    /**
     * 最近一条消息内容
     */
    private String lastMessage;

    /**
     * 最近一条消息时间
     */
    private LocalDateTime lastTime;

    /**
     * 用户端未读消息数
     */
    private Integer unreadForUser;

    /**
     * 客服端未读消息数
     */
    private Integer unreadForAgent;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
