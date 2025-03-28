package com.ruoyi.web.controller.care;

import com.ruoyi.care.service.RegistrationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.care.domain.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/28 9:47
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/care/registration")
public class RegistrationController extends BaseController {
    private final RegistrationService registrationService;

    /**
     * 获取所有活动报名信息
     *
     * @return 报名信息列表
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllRegistrations() {
        List<Registration> registrations = registrationService.getAllRegistrations();
        return getDataTable(registrations);
    }

    /**
     * 根据报名ID获取活动报名信息
     *
     * @param registrationId 报名ID
     * @return 报名详情
     */
    @GetMapping("/get")
    public AjaxResult getRegistrationById(@RequestParam Long registrationId) {
        return success(registrationService.getRegistrationById(registrationId));
    }

    /**
     * 添加活动报名信息
     *
     * @param registration 报名对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addRegistration(@RequestBody Registration registration) {
        return toAjax(registrationService.addRegistration(registration));
    }

    /**
     * 更新活动报名信息
     *
     * @param registration 报名对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult updateRegistration(@RequestBody Registration registration) {
        return toAjax(registrationService.updateRegistration(registration));
    }

    /**
     * 删除活动报名信息
     *
     * @param registrationId 报名ID
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult deleteRegistration(@RequestParam Long registrationId) {
        return toAjax(registrationService.deleteRegistration(registrationId));
    }

    /**
     * 根据活动ID获取报名列表
     *
     * @param activityId 活动ID
     * @return 报名信息列表
     */
    @GetMapping("/listByActivityId")
    public TableDataInfo listByActivityId(@RequestParam Long activityId) {
        List<Registration> registrations = registrationService.getRegistrationsByActivityId(activityId);
        return getDataTable(registrations);
    }

    /**
     * 根据用户ID获取报名列表
     *
     * @param userId 用户ID
     * @return 报名信息列表
     */
    @GetMapping("/listByUserId")
    public TableDataInfo listByUserId(@RequestParam Long userId) {
        List<Registration> registrations = registrationService.getRegistrationsByUserId(userId);
        return getDataTable(registrations);
    }
}
