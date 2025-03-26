package com.ruoyi.care.mapper;

import com.ruoyi.care.domain.CommunityActivity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:10
 */
@Mapper
public interface CommunityActivityMapper {
    /**
     * 获取所有社区活动
     * @return 活动列表
     */
    List<CommunityActivity> getAllCommunityActivities();

    /**
     * 根据ID获取社区活动详情
     * @param activityId 活动ID
     * @return 活动详情
     */
    CommunityActivity getCommunityActivityById(Long activityId);

    /**
     * 添加社区活动
     * @param communityActivity 活动信息
     * @return 影响行数
     */
    int addCommunityActivity(CommunityActivity communityActivity);

    /**
     * 更新社区活动
     * @param communityActivity 活动信息
     * @return 影响行数
     */
    int updateCommunityActivity(CommunityActivity communityActivity);

    /**
     * 删除社区活动
     * @param activityId 活动ID
     * @return 影响行数
     */
    int deleteCommunityActivity(Long activityId);
}
