package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户每日唯一访问记录
 */
@Data
@TableName("t_visit_log")
public class VisitLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 访问日期（按天唯一）
     */
    private LocalDate visitDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
