package com.ruoyi.care.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @Author 范佳兴
 * @date 2025/3/25 17:45
 */
@TableName("ecs_activity_registration")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventRegistration implements Serializable {
    /**
     * 报名ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long registrationId;

    /**
     * 关联老年人ID
     */
    private Long elderlyId;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 报名时间
     */
    private LocalDateTime registrationTime;

    /**
     * 预约状态（1-已预约，0-已取消）
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
