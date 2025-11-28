package com.puxinxiaolin.adopt.entity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 宠物实体类
 */
@Data
@TableName("t_pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 宠物ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 宠物名称
     */
    private String name;

    /**
     * 宠物种类 (dog:狗 cat:猫 other:其他)
     */
    private String category;

    /**
     * 品种
     */
    private String breed;

    /**
     * 性别 (0:未知 1:公 2:母)
     */
    private Integer gender;

    /**
     * 年龄（月）
     */
    private Integer age;

    /**
     * 体重（kg）
     */
    private BigDecimal weight;

    /**
     * 毛色
     */
    private String color;

    /**
     * 是否绝育
     */
    private Boolean neutered;

    /**
     * 是否接种疫苗
     */
    private Boolean vaccinated;

    /**
     * 健康状态 (healthy:健康 sick:生病 recovering:康复中)
     */
    private String healthStatus;

    /**
     * 健康描述
     */
    private String healthDescription;

    /**
     * 性格特点
     */
    private String personality;

    /**
     * 宠物描述
     */
    private String description;

    /**
     * 宠物图片URL（JSON数组）
     */
    private String images;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 救助时间
     */
    private LocalDate rescueDate;

    /**
     * 救助地点
     */
    private String rescueLocation;

    /**
     * 领养要求
     */
    private String adoptionRequirements;

    /**
     * 领养状态 (available:可领养 pending:待审核 adopted:已领养 reserved:已预定) <br />
     * <p> status 流程: 可领养 -> 已预订 -> （仅管理员端）待审核 -> 已领养 <p /> 
     */
    private String adoptionStatus;

    /**
     * 领养者ID
     */
    private Long adoptedBy;

    /**
     * 上架状态 (0:下架 1:上架)
     */
    private Integer shelfStatus;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞次数
     */
    private Integer likeCount;

    /**
     * 收藏次数
     */
    private Integer favoriteCount;

    /**
     * 申请次数
     */
    private Integer applicationCount;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}

