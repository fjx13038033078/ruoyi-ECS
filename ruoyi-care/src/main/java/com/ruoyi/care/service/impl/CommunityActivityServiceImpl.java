package com.ruoyi.care.service.impl;

import com.ruoyi.care.domain.CommunityActivity;
import com.ruoyi.care.mapper.CommunityActivityMapper;
import com.ruoyi.care.service.CommunityActivityService;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:25
 */
@Service
@RequiredArgsConstructor
public class CommunityActivityServiceImpl implements CommunityActivityService {
    private final CommunityActivityMapper communityActivityMapper;

    /**
     * 获取所有社区活动
     * @return 社区活动列表
     */
    @Override
    public List<CommunityActivity> getAllCommunityActivities() {
        return communityActivityMapper.getAllCommunityActivities();
    }

    /**
     * 根据ID获取社区活动详情
     * @param activityId 社区活动ID
     * @return 社区活动详情
     */
    @Override
    public CommunityActivity getCommunityActivityById(Long activityId) {
        return communityActivityMapper.getCommunityActivityById(activityId);
    }

    /**
     * 添加社区活动
     * @param communityActivity 社区活动信息
     * @return 是否添加成功
     */
    @Override
    @Transactional
    public boolean addCommunityActivity(CommunityActivity communityActivity) {
//        communityActivity.setOrganizerId(SecurityUtils.getUserId());
        int rows = communityActivityMapper.addCommunityActivity(communityActivity);
        return rows > 0;
    }

    /**
     * 更新社区活动
     * @param communityActivity 社区活动信息
     * @return 是否更新成功
     */
    @Override
    @Transactional
    public boolean updateCommunityActivity(CommunityActivity communityActivity) {
        Long userId = SecurityUtils.getUserId();
//        if (!communityActivity.getOrganizerId().equals(userId) && userId != 1) {
//            throw new RuntimeException("用户仅能修改自己组织的活动");
//        }
        int rows = communityActivityMapper.updateCommunityActivity(communityActivity);
        return rows > 0;
    }

    /**
     * 删除社区活动
     * @param activityId 社区活动ID
     * @return 是否删除成功
     */
    @Override
    @Transactional
    public boolean deleteCommunityActivity(Long activityId) {
        CommunityActivity activity = communityActivityMapper.getCommunityActivityById(activityId);
        Long userId = SecurityUtils.getUserId();
//        if (!activity.getOrganizerId().equals(userId) && userId != 1) {
//            throw new RuntimeException("用户仅能删除自己组织的活动");
//        }
        int rows = communityActivityMapper.deleteCommunityActivity(activityId);
        return rows > 0;
    }
}
