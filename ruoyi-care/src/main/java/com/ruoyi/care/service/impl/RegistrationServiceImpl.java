package com.ruoyi.care.service.impl;

import com.ruoyi.care.domain.Registration;
import com.ruoyi.care.mapper.RegistrationMapper;
import com.ruoyi.care.service.RegistrationService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * @Author 范佳兴
 * @date 2025/3/28 9:27
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

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
            throw new RuntimeException("您已预约该活动，不能重复预约！");
        }

        // 统计有效预约数量
        long activeCount = registrationMapper
                .getRegistrationsByUserId(userId)
                .stream()
                .filter(r -> r.getStatus() == 1)
                .count();
        if (activeCount >= 3) {
            throw new RuntimeException("您已预约3个活动，不能再预约！");
        }
    }
}
