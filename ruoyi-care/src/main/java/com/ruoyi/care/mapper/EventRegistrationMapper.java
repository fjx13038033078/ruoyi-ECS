package com.ruoyi.care.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.ruoyi.care.domain.EventRegistration;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:51
 */
@Mapper
public interface EventRegistrationMapper {
    /**
     * 获取所有活动报名
     *
     * @return 活动报名列表
     */
    List<EventRegistration> getAllRegistrations();

    /**
     * 根据报名ID获取活动报名详情
     *
     * @param registrationId 报名ID
     * @return 活动报名详情
     */
    EventRegistration getRegistrationById(Long registrationId);

    /**
     * 根据老年人ID获取其所有报名记录
     *
     * @param elderlyId 老年人ID
     * @return 活动报名列表
     */
    List<EventRegistration> getRegistrationsByElderlyId(Long elderlyId);

    /**
     * 添加活动报名
     *
     * @param registration 活动报名信息
     * @return 影响行数
     */
    int addRegistration(EventRegistration registration);

    /**
     * 更新活动报名
     *
     * @param registration 活动报名信息
     * @return 影响行数
     */
    int updateRegistration(EventRegistration registration);

    /**
     * 删除活动报名
     *
     * @param registrationId 报名ID
     * @return 影响行数
     */
    int deleteRegistration(Long registrationId);
}
