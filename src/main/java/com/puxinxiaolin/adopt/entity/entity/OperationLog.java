package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@TableName("t_operation_log")
public class OperationLog implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 日志ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 操作用户ID */
    private Long userId;
    
    /** 操作用户名 */
    private String username;
    
    /** 操作模块 */
    private String module;
    
    /** 操作类型 (create:新增 update:修改 delete:删除 query:查询) */
    private String operationType;
    
    /** 操作描述 */
    private String description;
    
    /** 请求方法 */
    private String method;
    
    /** 请求参数 */
    private String params;
    
    /** IP地址 */
    private String ip;
    
    /** 操作地点 */
    private String location;
    
    /** 操作状态 (0:失败 1:成功) */
    private Integer status;
    
    /** 错误消息 */
    private String errorMessage;
    
    /** 操作时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

