package com.ruoyi.care.service;

import com.ruoyi.care.domain.Registration;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/28 9:22
 */
public interface RegistrationService {
    /**
     * 获取所有活动报名信息
     * @return 所有活动报名信息列表
     */
    List<Registration> getAllRegistrations();

    /**
     * 根据报名ID获取活动报名信息
     * @param registrationId 报名ID
     * @return 活动报名信息
     */
    Registration getRegistrationById(Long registrationId);

    /**
     * 判断用户是否已报名该活动
     *
     * @param activityId 活动ID
     * @param elderlyId  用户ID
     * @return 已报名返回 true，否则返回 false
     */
    boolean isAlreadyRegistered(Long activityId, Long elderlyId);

    /**
     * 添加活动报名信息
     * @param activityRegistration 待添加的活动报名信息
     * @return 添加成功返回 true，否则返回 false
     */
    boolean addRegistration(Registration activityRegistration);

    /**
     * 更新活动报名信息
     * @param activityRegistration 待更新的活动报名信息
     * @return 更新成功返回 true，否则返回 false
     */
    boolean updateRegistration(Registration activityRegistration);

    /**
     * 删除活动报名信息
     * @param registrationId 待删除的报名ID
     * @return 删除成功返回 true，否则返回 false
     */
    boolean deleteRegistration(Long registrationId);

    /**
     * 根据活动ID获取报名信息
     * @param activityId 活动ID
     * @return 报名信息列表
     */
    List<Registration> getRegistrationsByActivityId(Long activityId);

    /**
     * 根据用户ID获取报名信息
     * @param elderlyId 用户ID
     * @return 报名信息列表
     */
    List<Registration> getRegistrationsByUserId(Long elderlyId);
}
