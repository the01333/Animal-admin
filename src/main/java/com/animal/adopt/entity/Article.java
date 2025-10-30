package com.animal.adopt.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章实体类（领养指南、科普文章、成功案例等）
 */
@Data
@TableName("t_article")
public class Article implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 文章ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 文章标题 */
    private String title;
    
    /** 文章分类 (guide:领养指南 knowledge:科普知识 success:成功案例 notice:公告) */
    private String category;
    
    /** 文章摘要 */
    private String summary;
    
    /** 封面图片 */
    private String coverImage;
    
    /** 文章内容 */
    private String content;
    
    /** 作者 */
    private String author;
    
    /** 浏览次数 */
    private Integer viewCount;
    
    /** 点赞次数 */
    private Integer likeCount;
    
    /** 发布状态 (0:草稿 1:已发布 2:已下架) */
    private Integer status;
    
    /** 发布时间 */
    private LocalDateTime publishTime;
    
    /** 排序号 */
    private Integer sortOrder;
    
    /** 创建人ID */
    private Long createBy;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}

