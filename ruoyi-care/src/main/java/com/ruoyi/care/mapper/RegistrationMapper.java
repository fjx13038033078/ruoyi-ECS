package com.ruoyi.care.mapper;

import com.ruoyi.care.domain.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/28 9:18
 */
@Mapper
public interface RegistrationMapper {
    /**
     * 查询所有活动报名信息
     *
     * @return 活动报名信息列表
     */
    List<Registration> getAllRegistrations();

    /**
     * 根据报名ID查询活动报名信息
     *
     * @param registrationId 报名ID
     * @return 活动报名信息
     */
    Registration getRegistrationById(Long registrationId);

    int checkUserRegistration(@Param("activityId") Long activityId, @Param("elderlyId") Long elderlyId);

    Registration getRegistrationByActivityAndUser(@Param("activityId") Long activityId,
                                                          @Param("elderlyId") Long elderlyId);

    /**
     * 添加活动报名信息
     *
     * @param activityRegistration 待添加的活动报名信息
     * @return 添加成功返回影响的行数，否则返回0
     */
    int addRegistration(Registration activityRegistration);

    /**
     * 更新活动报名信息
     *
     * @param activityRegistration 待更新的活动报名信息
     * @return 更新成功返回影响的行数，否则返回0
     */
    int updateRegistration(Registration activityRegistration);

    /**
     * 删除活动报名信息
     *
     * @param registrationId 待删除的报名ID
     * @return 删除成功返回影响的行数，否则返回0
     */
    int deleteRegistration(Long registrationId);

    /**
     * 根据活动ID查询报名信息
     *
     * @param activityId 活动ID
     * @return 活动报名信息
     */
    List<Registration> getRegistrationsByActivityId(Long activityId);

    /**
     * 根据用户ID查询报名信息
     *
     * @param elderlyId 用户ID
     * @return 活动报名信息
     */
    List<Registration> getRegistrationsByUserId(Long elderlyId);
}
