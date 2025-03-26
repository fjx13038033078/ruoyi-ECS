package com.ruoyi.web.controller.care;

import com.ruoyi.care.domain.ServiceRequest;
import com.ruoyi.care.service.ServiceRequestService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/26 16:58
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/service/request")
public class ServiceRequestController extends BaseController {
    private final ServiceRequestService serviceRequestService;

    /**
     * 获取所有服务请求
     */
    @GetMapping("/listAll")
    public TableDataInfo listAllServiceRequests() {
        List<ServiceRequest> allRequests = serviceRequestService.getAllServiceRequests();
        return getDataTable(allRequests);
    }

    /**
     * 获取服务请求详情
     */
    @GetMapping("/detail")
    public AjaxResult getServiceRequestById(@RequestParam Long requestId) {
        return success(serviceRequestService.getServiceRequestById(requestId));
    }

    /**
     * 更新服务请求状态
     */
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody ServiceRequest serviceRequest) {
        return toAjax(serviceRequestService.updateStatus(serviceRequest));
    }

    /**
     * 更新服务请求详情
     */
    @PostMapping("/updateDetails")
    public AjaxResult updateServiceDetails(@RequestBody ServiceRequest serviceRequest) {
        return toAjax(serviceRequestService.updateServiceDetails(serviceRequest));
    }

    /**
     * 删除服务请求
     */
    @GetMapping("/delete")
    public AjaxResult deleteServiceRequest(@RequestParam Long requestId) {
        return toAjax(serviceRequestService.deleteServiceRequest(requestId));
    }

    /**
     * 添加服务请求
     * @param serviceRequest 服务请求对象
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult addServiceRequest(@RequestBody ServiceRequest serviceRequest) {
        return toAjax(serviceRequestService.addServiceRequest(serviceRequest));
    }

    /**
     * 更新服务请求
     * @param serviceRequest 服务请求对象
     * @return 操作结果
     */
    @PostMapping("/update")
    public AjaxResult updateServiceRequest(@RequestBody ServiceRequest serviceRequest) {
        return toAjax(serviceRequestService.updateServiceRequest(serviceRequest));
    }
}
