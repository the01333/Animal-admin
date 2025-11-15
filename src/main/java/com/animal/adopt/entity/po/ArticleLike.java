package com.animal.adopt.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章点赞实体类
 */
@Data
@TableName("t_article_like")
public class ArticleLike implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 点赞ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 文章ID */
    private Long articleId;
    
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;
}
