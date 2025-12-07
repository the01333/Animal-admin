package com.puxinxiaolin.adopt.entity.vo;

import lombok.Data;

/**
 * 字典项 VO
 */
@Data
public class DictItemVO {

    private Long id;

    private String dictType;

    private String dictKey;

    private String dictLabel;

    private Integer sortOrder;

    private Integer status;

    private String remark;
}
