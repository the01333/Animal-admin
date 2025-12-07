package com.puxinxiaolin.adopt.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典项创建/更新 DTO
 */
@Data
public class DictItemDTO {

    /**
     * 字典类型, 例如 pet_category / adoption_status / health_status / article_category
     */
    @NotBlank(message = "字典类型不能为空")
    private String dictType;

    /**
     * 字典键, 例如 cat / available / GUIDE
     */
    @NotBlank(message = "字典键不能为空")
    private String dictKey;

    /**
     * 显示名称, 例如 猫咪 / 可领养 / 领养指南
     */
    @NotBlank(message = "显示名称不能为空")
    private String dictLabel;

    /**
     * 排序号, 数字越小越靠前
     */
    private Integer sortOrder;

    /**
     * 状态：1 启用, 0 禁用
     */
    private Integer status;

    /**
     * 备注说明
     */
    private String remark;
}
