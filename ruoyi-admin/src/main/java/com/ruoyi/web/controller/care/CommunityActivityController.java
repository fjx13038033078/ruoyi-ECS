package com.ruoyi.web.controller.care;

import com.ruoyi.care.domain.CommunityActivity;
import com.ruoyi.care.service.CommunityActivityService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:26
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/community/activity")
public class CommunityActivityController extends BaseController {
    private final CommunityActivityService communityActivityService;

    /**
     * 获取所有社区活动
     * @return 社区活动列表
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllCommunityActivities() {
        List<CommunityActivity> allActivities = communityActivityService.getAllCommunityActivities();
        return getDataTable(allActivities);
    }

    /**
     * 根据ID获取社区活动详情
     * @param activityId 社区活动ID
     * @return 社区活动详情
     */
    @GetMapping("/detail")
    public AjaxResult getCommunityActivityById(@RequestParam Long activityId) {
        return success(communityActivityService.getCommunityActivityById(activityId));
    }

    /**
     * 添加社区活动
     * @param communityActivity 社区活动对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addCommunityActivity(@RequestBody CommunityActivity communityActivity) {
        return toAjax(communityActivityService.addCommunityActivity(communityActivity));
    }

    /**
     * 更新社区活动
     * @param communityActivity 社区活动对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult updateCommunityActivity(@RequestBody CommunityActivity communityActivity) {
        return toAjax(communityActivityService.updateCommunityActivity(communityActivity));
    }

    /**
     * 删除社区活动
     * @param activityId 社区活动ID
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult deleteCommunityActivity(@RequestParam Long activityId) {
        return toAjax(communityActivityService.deleteCommunityActivity(activityId));
    }
}
