package com.ruoyi.care.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author 范佳兴
 * @date 2025/4/16 15:57
 */
@TableName("ecs_home")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class HomeRelation implements Serializable {
    /**
     * 家属关系主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long homeId;

    /**
     * 老年人ID
     */
    private Long userId;

    @TableField(exist = false)
    private String userName;

    /**
     * 家属ID
     */
    private Long dependentsId;

    @TableField(exist = false)
    private String dependentsName;

    /**
     * 与登记老年人关系（1-配偶，2-子女，3-兄弟姐妹，4-其他）
     */
    private Integer relation;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
