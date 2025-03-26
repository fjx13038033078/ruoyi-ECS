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
import java.time.LocalDate;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:04
 */
@TableName("ecs_health_record")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class HealthRecord implements Serializable {
    /**
     * 记录ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 关联老年人ID
     */
    private Long elderlyId;

    /**
     * 关联老年人姓名
     */
    private String userName;

    /**
     * 记录日期
     */
    private LocalDate recordDate;

    /**
     * 血压（如120/80）
     */
    private String bloodPressure;

    /**
     * 血糖（单位：mmol/L）
     */
    private Double bloodSugar;

    /**
     * 体温（单位：℃）
     */
    private Double temperature;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 健康附件
     */
    private String fileName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
