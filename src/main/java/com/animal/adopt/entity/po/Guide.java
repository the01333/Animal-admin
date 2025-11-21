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
 * 领养指南实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_guide")
public class Guide {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 摘要
     */
    private String excerpt;
    
    /**
     * Markdown格式的内容
     */
    private String content;
    
    /**
     * 封面图片URL
     */
    private String image;
    
    /**
     * 分类
     */
    private String category;
    
    /**
     * 浏览次数
     */
    private Integer views;
    
    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
