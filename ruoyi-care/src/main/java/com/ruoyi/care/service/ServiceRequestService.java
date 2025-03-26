package com.ruoyi.care.service;

import com.ruoyi.care.domain.ServiceRequest;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/26 16:56
 */
public interface ServiceRequestService {
    /**
     * 获取所有服务请求
     *
     * @return 服务请求列表
     */
    List<ServiceRequest> getAllServiceRequests();

    /**
     * 根据ID获取服务请求
     *
     * @param requestId 服务请求ID
     * @return 服务请求详情
     */
    ServiceRequest getServiceRequestById(Long requestId);

    /**
     * 更新服务请求状态
     *
     * @param serviceRequest 服务请求对象
     * @return 是否更新成功
     */
    boolean updateStatus(ServiceRequest serviceRequest);

    /**
     * 更新服务请求详情
     *
     * @param serviceRequest 服务请求对象
     * @return 是否更新成功
     */
    boolean updateServiceDetails(ServiceRequest serviceRequest);

    /**
     * 删除服务请求
     *
     * @param requestId 服务请求ID
     * @return 是否删除成功
     */
    boolean deleteServiceRequest(Long requestId);

    /**
     * 添加服务请求
     * @param serviceRequest 服务请求对象
     * @return 是否添加成功
     */
    boolean addServiceRequest(ServiceRequest serviceRequest);

    /**
     * 更新服务请求
     * @param serviceRequest 服务请求对象
     * @return 是否更新成功
     */
    boolean updateServiceRequest(ServiceRequest serviceRequest);

}
