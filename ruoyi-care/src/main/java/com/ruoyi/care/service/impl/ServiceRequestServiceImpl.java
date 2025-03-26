package com.ruoyi.care.service.impl;

import com.ruoyi.care.domain.ServiceRequest;
import com.ruoyi.care.service.ServiceRequestService;
import com.ruoyi.care.mapper.ServiceRequestMapper;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/26 16:57
 */
@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {
    private final ServiceRequestMapper serviceRequestMapper;

    /**
     * 获取所有服务请求
     */
    @Override
    public List<ServiceRequest> getAllServiceRequests() {
        return serviceRequestMapper.getAllServiceRequests();
    }

    /**
     * 根据ID获取服务请求详情
     */
    @Override
    public ServiceRequest getServiceRequestById(Long requestId) {
        return serviceRequestMapper.getServiceRequestById(requestId);
    }

    /**
     * 更新服务请求状态
     */
    @Override
    @Transactional
    public boolean updateStatus(ServiceRequest serviceRequest) {
        int rows = serviceRequestMapper.updateStatus(serviceRequest);
        return rows > 0;
    }

    /**
     * 更新服务请求详情
     */
    @Override
    @Transactional
    public boolean updateServiceDetails(ServiceRequest serviceRequest) {
        int rows = serviceRequestMapper.updateServiceDetails(serviceRequest);
        return rows > 0;
    }

    /**
     * 删除服务请求
     */
    @Override
    @Transactional
    public boolean deleteServiceRequest(Long requestId) {
        int rows = serviceRequestMapper.deleteServiceRequest(requestId);
        return rows > 0;
    }

    /**
     * 添加服务请求
     * @param serviceRequest 服务请求对象
     * @return 是否添加成功
     */
    @Override
    @Transactional
    public boolean addServiceRequest(ServiceRequest serviceRequest) {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        serviceRequest.setElderlyId(userId);
        serviceRequest.setStatus(0); // 默认状态
        int rows = serviceRequestMapper.addServiceRequest(serviceRequest);
        return rows > 0;
    }

    /**
     * 更新服务请求
     * @param serviceRequest 服务请求对象
     * @return 是否更新成功
     */
    @Override
    @Transactional
    public boolean updateServiceRequest(ServiceRequest serviceRequest) {
        int rows = serviceRequestMapper.updateServiceRequest(serviceRequest);
        return rows > 0;
    }
}
