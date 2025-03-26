package com.ruoyi.care.mapper;

import com.ruoyi.care.domain.ServiceRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author 范佳兴
 * @date 2025/3/26 16:33
 */
@Mapper
public interface ServiceRequestMapper {
    /**
     * 获取所有服务请求
     *
     * @return 服务请求列表
     */
    List<ServiceRequest> getAllServiceRequests();

    /**
     * 根据请求ID获取服务请求详情
     *
     * @param requestId 请求ID
     * @return 服务请求详情
     */
    ServiceRequest getServiceRequestById(Long requestId);

    /**
     * 根据老人ID获取服务请求列表
     *
     * @param elderlyId 老人ID
     * @return 服务请求列表
     */
    List<ServiceRequest> getServiceRequestsByElderlyId(Long elderlyId);

    /**
     * 添加服务请求
     *
     * @param serviceRequest 服务请求信息
     * @return 影响行数
     */
    int addServiceRequest(ServiceRequest serviceRequest);

    /**
     * 更新服务请求
     *
     * @param serviceRequest 服务请求信息
     * @return 影响行数
     */
    int updateServiceRequest(ServiceRequest serviceRequest);

    /**
     * 删除服务请求
     *
     * @param requestId 请求ID
     * @return 影响行数
     */
    int deleteServiceRequest(Long requestId);

    /**
     * 仅修改申请状态（用于用户取消申请或管理员审核）
     */
    int updateStatus(ServiceRequest serviceRequest);

    /**
     * 修改状态、服务时间、服务人员、服务详情（用于完成服务时填写）
     */
    int updateServiceDetails(ServiceRequest serviceRequest);
}
