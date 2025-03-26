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
import java.time.LocalDateTime;

/**
 * @Author 范佳兴
 * @date 2025/3/26 16:32
 */
@TableName("service_request")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ServiceRequest implements Serializable {

    /**
     * 请求ID，主键，自增
     */
    @TableId(type = IdType.AUTO)
    private Long requestId;

    /**
     * 关联老年人ID
     */
    private Long elderlyId;

    /**
     * 服务类型 (1: 家政服务, 2: 医疗护理, 3: 日常陪伴)
     */
    private Integer serviceType;

    /**
     * 预约时间
     */
    private LocalDateTime requestTime;

    /**
     * 请求状态 (0: 待审核, 1: 已批准, 2: 已完成, 3: 已取消)
     */
    private Integer status;

    /**
     * 服务时间
     */
    private LocalDateTime serviceTime;

    /**
     * 服务人员
     */
    private String servicePersonnel;

    /**
     * 服务详细信息
     */
    private String details;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
