package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一收藏表实体类
 * 
 * 通过 target_type 字段区分不同类型的目标对象（宠物、指南、故事）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_favorite")
public class FavoriteUnified implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 目标ID（宠物ID/指南ID/故事ID）
     */
    @TableField("target_id")
    private Long targetId;

    /**
     * 目标类型：PET-宠物, GUIDE-指南, STORY-故事
     */
    @TableField("target_type")
    private String targetType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
