package com.ruoyi.care.service;

import com.ruoyi.care.domain.CommunityActivity;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:24
 */
public interface CommunityActivityService {
    /**
     * 查询所有社区活动
     * @return 社区活动列表
     */
    List<CommunityActivity> getAllCommunityActivities();

    /**
     * 根据ID查询社区活动
     * @param activityId 社区活动ID
     * @return 社区活动详情
     */
    CommunityActivity getCommunityActivityById(Long activityId);

    /**
     * 添加社区活动
     * @param communityActivity 社区活动对象
     * @return 是否添加成功
     */
    boolean addCommunityActivity(CommunityActivity communityActivity);

    /**
     * 更新社区活动
     * @param communityActivity 社区活动对象
     * @return 是否更新成功
     */
    boolean updateCommunityActivity(CommunityActivity communityActivity);

    /**
     * 删除社区活动
     * @param activityId 社区活动ID
     * @return 是否删除成功
     */
    boolean deleteCommunityActivity(Long activityId);
}
