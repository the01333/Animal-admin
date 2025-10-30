package com.animal.adopt.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收藏实体类
 */
@Data
@TableName("t_favorite")
public class Favorite implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 收藏ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 宠物ID */
    private Long petId;
    
    /** 收藏时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

