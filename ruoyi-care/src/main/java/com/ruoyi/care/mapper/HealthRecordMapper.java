package com.ruoyi.care.mapper;

import com.ruoyi.care.domain.HealthRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:05
 */
@Mapper
public interface HealthRecordMapper {
    /**
     * 获取所有健康记录
     * @return 健康记录列表
     */
    List<HealthRecord> getAllHealthRecords();

    /**
     * 根据ID获取健康记录详情
     * @param recordId 记录ID
     * @return 健康记录详情
     */
    HealthRecord getHealthRecordById(Long recordId);

    /**
     * 根据老人ID获取健康记录列表
     * @param elderlyId 老人ID
     * @return 健康记录列表
     */
    List<HealthRecord> getHealthRecordsByElderlyId(Long elderlyId);

    /**
     * 添加健康记录
     * @param healthRecord 健康记录信息
     * @return 影响行数
     */
    int addHealthRecord(HealthRecord healthRecord);

    /**
     * 更新健康记录
     * @param healthRecord 健康记录信息
     * @return 影响行数
     */
    int updateHealthRecord(HealthRecord healthRecord);

    /**
     * 删除健康记录
     * @param recordId 记录ID
     * @return 影响行数
     */
    int deleteHealthRecord(Long recordId);
}
