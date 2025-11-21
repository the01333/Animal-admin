package com.animal.adopt.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 指南点赞记录实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_guide_like")
public class GuideLike {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 指南ID
     */
    @TableField("guide_id")
    private Long guideId;
    
    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
