package com.ruoyi.care.service;

import com.ruoyi.care.domain.HealthRecord;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:17
 */
public interface HealthRecordService {
    /**
     * 查询所有健康记录
     * @return 健康记录列表
     */
    List<HealthRecord> getAllHealthRecords();

    /**
     * 根据ID查询健康记录
     * @param recordId 健康记录ID
     * @return 健康记录详情
     */
    HealthRecord getHealthRecordById(Long recordId);

    /**
     * 添加健康记录
     * @param healthRecord 健康记录对象
     * @return 是否添加成功
     */
    boolean addHealthRecord(HealthRecord healthRecord);

    /**
     * 更新健康记录
     * @param healthRecord 健康记录对象
     * @return 是否更新成功
     */
    boolean updateHealthRecord(HealthRecord healthRecord);

    /**
     * 删除健康记录
     * @param recordId 健康记录ID
     * @return 是否删除成功
     */
    boolean deleteHealthRecord(Long recordId);
}
