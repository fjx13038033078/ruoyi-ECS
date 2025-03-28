package com.ruoyi.care.service.impl;

import com.ruoyi.care.domain.CommunityActivity;
import com.ruoyi.care.domain.Registration;
import com.ruoyi.care.mapper.RegistrationMapper;
import com.ruoyi.care.service.RegistrationService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author 范佳兴
 * @date 2025/3/28 9:27
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    private final RegistrationMapper registrationMapper;

    private final CommunityActivityServiceImpl communityActivityService;

    private final ISysUserService iSysUserService;

    private final ISysRoleService iSysRoleService;

    @Override
    public List<Registration> getAllRegistrations() {
        Long userId = SecurityUtils.getUserId();
        String role = iSysRoleService.selectStringRoleByUserId(userId);
        if (role.equals("admin")) {
            startPage();
            List<Registration> allActivityRegistrations = registrationMapper.getAllRegistrations();
            fillRegistration(allActivityRegistrations);
            return allActivityRegistrations;
        } else {
            startPage();
            List<Registration> activityRegistrationsByUserId = registrationMapper.getRegistrationsByUserId(userId);
            fillRegistration(activityRegistrationsByUserId);
            return activityRegistrationsByUserId;
        }
    }

    @Override
    public Registration getRegistrationById(Long registrationId) {
        return registrationMapper.getRegistrationById(registrationId);
    }

    @Override
    public boolean isAlreadyRegistered(Long activityId, Long elderlyId) {
        int count = registrationMapper.checkUserRegistration(activityId, elderlyId);
        return count > 0;
    }

    @Override
    @Transactional
    public boolean addRegistration(Registration activityRegistration) {
        Long userId = SecurityUtils.getUserId();
        activityRegistration.setElderlyId(userId);
        Long activityId = activityRegistration.getActivityId();
        // 执行预约校验
        validateActivityRegistration(activityId, userId);
        Registration registrationByActivityAndUser = registrationMapper.getRegistrationByActivityAndUser(activityId, userId);
        if (registrationByActivityAndUser != null && registrationByActivityAndUser.getStatus() == 0) {
            activityRegistration.setRegistrationId(registrationByActivityAndUser.getRegistrationId());
            activityRegistration.setStatus(1);  // 1表示预约成功
            activityRegistration.setRegisterTime(LocalDateTime.now());
            int rows = registrationMapper.updateRegistration(activityRegistration);
            return rows > 0;
        } else if (registrationByActivityAndUser == null) {
            activityRegistration.setStatus(1);
            int rows = registrationMapper.addRegistration(activityRegistration);
            return rows > 0;
        }
        return false;
    }

    @Override
    public boolean updateRegistration(Registration activityRegistration) {
        int rows = registrationMapper.updateRegistration(activityRegistration);
        return rows > 0;
    }

    @Override
    public boolean deleteRegistration(Long registrationId) {
        int rows = registrationMapper.deleteRegistration(registrationId);
        return rows > 0;
    }

    @Override
    public List<Registration> getRegistrationsByActivityId(Long activityId) {
        return registrationMapper.getRegistrationsByActivityId(activityId);
    }

    @Override
    public List<Registration> getRegistrationsByUserId(Long elderlyId) {
        return registrationMapper.getRegistrationsByUserId(elderlyId);
    }

    @Override
    public List<Map<String, Integer>> getTop5ActivitiesByRegistrations() {
        // 获取报名人数最多的前 5 个活动的 activity_id
        List<Long> top5ActivityIds = registrationMapper.getTop5ActivitiesByRegistrations();

        // 最终返回的结果
        List<Map<String, Integer>> result = new ArrayList<>();

        // 遍历获取每个活动的标题，并转换为 Map
        for (Long activityId : top5ActivityIds) {
            String activityTitle = communityActivityService.getCommunityActivityById(activityId).getTitle();
            int registrationCount = registrationMapper.getRegistrationCountByActivityId(activityId);

            // 创建 Map 并添加到结果列表
            Map<String, Integer> activityMap = new HashMap<>();
            activityMap.put(activityTitle, registrationCount);
            result.add(activityMap);
        }

        return result;
    }

    private void fillRegistration(List<Registration> registrations) {
        for (Registration registration : registrations) {
            Long userId = registration.getElderlyId();
            String userName = iSysUserService.selectUserById(userId).getNickName();
            Long activityId = registration.getActivityId();
            String activityName = communityActivityService.getCommunityActivityById(activityId).getTitle();
            registration.setUserName(userName);
            registration.setActivityName(activityName);
        }
    }

    /**
     * 验证预约活动
     *
     * @param activityId
     * @param userId
     */
    private void validateActivityRegistration(Long activityId, Long userId) {

        // 查询该用户该活动的预约记录（包含已取消）
        Registration existingRegistration = registrationMapper
                .getRegistrationByActivityAndUser(activityId, userId);

        // 校验是否已预约
        if (existingRegistration != null && existingRegistration.getStatus() != 0) {
            throw new RuntimeException("您已报名该活动，不能重复报名！");
        }

        // 统计有效预约数量
        long activeCount = registrationMapper
                .getRegistrationsByUserId(userId)
                .stream()
                .filter(r -> r.getStatus() == 1)
                .count();
        if (activeCount >= 3) {
            throw new RuntimeException("您已报名3个活动，不能再报名！");
        }
    }
}
