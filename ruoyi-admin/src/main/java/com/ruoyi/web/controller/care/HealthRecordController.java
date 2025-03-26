package com.ruoyi.web.controller.care;

import com.ruoyi.care.domain.HealthRecord;
import com.ruoyi.care.service.HealthRecordService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/25 17:20
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/health/record")
public class HealthRecordController extends BaseController {
    private final HealthRecordService healthRecordService;

    /**
     * 获取所有健康记录
     * @return 健康记录列表
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllHealthRecords() {
        List<HealthRecord> allRecords = healthRecordService.getAllHealthRecords();
        return getDataTable(allRecords);
    }

    /**
     * 根据ID获取健康记录详情
     * @param recordId 健康记录ID
     * @return 健康记录详情
     */
    @GetMapping("/detail")
    public AjaxResult getHealthRecordById(@RequestParam Long recordId) {
        return success(healthRecordService.getHealthRecordById(recordId));
    }

    /**
     * 添加健康记录
     * @param healthRecord 健康记录对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addHealthRecord(@RequestBody HealthRecord healthRecord) {
        return toAjax(healthRecordService.addHealthRecord(healthRecord));
    }

    /**
     * 更新健康记录
     * @param healthRecord 健康记录对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult updateHealthRecord(@RequestBody HealthRecord healthRecord) {
        return toAjax(healthRecordService.updateHealthRecord(healthRecord));
    }

    /**
     * 删除健康记录
     * @param recordId 健康记录ID
     * @return 操作结果
     */
    @GetMapping("/delete")
    public AjaxResult deleteHealthRecord(@RequestParam Long recordId) {
        return toAjax(healthRecordService.deleteHealthRecord(recordId));
    }
}
