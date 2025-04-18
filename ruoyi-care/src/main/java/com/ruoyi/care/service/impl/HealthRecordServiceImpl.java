package com.ruoyi.care.service.impl;

import com.ruoyi.care.domain.HealthRecord;
import com.ruoyi.care.domain.HomeRelation;
import com.ruoyi.care.mapper.HealthRecordMapper;
import com.ruoyi.care.service.HealthRecordService;
import com.ruoyi.care.service.HomeRelationService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:18
 */
@Service
@RequiredArgsConstructor
public class HealthRecordServiceImpl implements HealthRecordService {

    private final HealthRecordMapper healthRecordMapper;

    private final ISysUserService iSysUserService;

    private final ISysRoleService iSysRoleService;

    private final HomeRelationService homeRelationService;

    /**
     * 获取所有健康记录
     *
     * @return 健康记录列表
     */
    @Override
    public List<HealthRecord> getAllHealthRecords() {
        Long userId = SecurityUtils.getUserId();
        String role = iSysRoleService.selectStringRoleByUserId(userId);

        startPage(); // 建议统一调用分页，避免每个分支都写一遍

        List<HealthRecord> allHealthRecords = new ArrayList<>();

        if ("admin".equals(role)) {
            allHealthRecords = healthRecordMapper.getAllHealthRecords();
        } else if ("home".equals(role)) {
            List<HomeRelation> homeRelations = homeRelationService.getHomeRelationByDependentsId(userId);
            for (HomeRelation relation : homeRelations) {
                Long elderlyId = relation.getUserId();
                List<HealthRecord> records = healthRecordMapper.getHealthRecordByElderlyId(elderlyId);
                allHealthRecords.addAll(records);
            }
        } else {
            allHealthRecords = healthRecordMapper.getHealthRecordByElderlyId(userId);
        }

        fillHealthRecord(allHealthRecords);
        return allHealthRecords;
    }

    /**
     * 根据ID获取健康记录详情
     *
     * @param recordId 健康记录ID
     * @return 健康记录详情
     */
    @Override
    public HealthRecord getHealthRecordById(Long recordId) {
        return healthRecordMapper.getHealthRecordById(recordId);
    }

    /**
     * 添加健康记录
     *
     * @param healthRecord 健康记录信息
     * @return 是否添加成功
     */
    @Override
    @Transactional
    public boolean addHealthRecord(HealthRecord healthRecord) {
        Long userId = SecurityUtils.getUserId();
        String role = iSysRoleService.selectStringRoleByUserId(userId);
        if (role.equals("admin")) {
            int rows = healthRecordMapper.addHealthRecord(healthRecord);
            return rows > 0;
        } else {
            healthRecord.setElderlyId(userId);
            int rows = healthRecordMapper.addHealthRecord(healthRecord);
            return rows > 0;
        }
    }

    /**
     * 更新健康记录
     *
     * @param healthRecord 健康记录信息
     * @return 是否更新成功
     */
    @Override
    @Transactional
    public boolean updateHealthRecord(HealthRecord healthRecord) {
        int rows = healthRecordMapper.updateHealthRecord(healthRecord);
        return rows > 0;
    }

    /**
     * 删除健康记录
     *
     * @param recordId 健康记录ID
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteHealthRecord(Long recordId) {
        int rows = healthRecordMapper.deleteHealthRecord(recordId);
        return rows > 0;
    }

    @Override
    public int countHighTemperatureToday() {
        return healthRecordMapper.countHighTemperatureToday();
    }

    @Override
    public int countHighBloodSugarToday() {
        return healthRecordMapper.countHighBloodSugarToday();
    }

    @Override
    public int countLowBloodSugarToday() {
        return healthRecordMapper.countLowBloodSugarToday();
    }

    /**
     * 填充健康记录信息
     *
     * @param healthRecords 健康记录列表
     */
    private void fillHealthRecord(List<HealthRecord> healthRecords) {
        for (HealthRecord healthRecord : healthRecords) {
            Long elderlyId = healthRecord.getElderlyId();
            if (elderlyId == null) {
                continue;
            }
            SysUser sysUser = iSysUserService.selectUserById(elderlyId);
            healthRecord.setUserName(sysUser.getNickName());
        }
    }
}
