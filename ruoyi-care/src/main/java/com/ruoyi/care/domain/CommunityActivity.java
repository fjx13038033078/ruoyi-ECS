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
 * @date 2025/3/25 17:09
 */
@TableName("ecs_community_activity")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CommunityActivity implements Serializable {
    /**
     * 活动ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long activityId;

    /**
     * 活动名称
     */
    private String title;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动时间
     */
    private LocalDate activityTime;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 最大参与人数
     */
    private Integer maxParticipants;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
