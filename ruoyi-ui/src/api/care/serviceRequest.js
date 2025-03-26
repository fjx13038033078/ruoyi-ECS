import request from '@/utils/request'

// 查询所有服务请求
export function listAllServiceRequests(query) {
  return request({
    url: '/service/request/listAll',
    method: 'get',
    params: query
  })
}

// 查询服务请求详情
export function getServiceRequestById(requestId) {
  return request({
    url: '/service/request/detail',
    method: 'get',
    params: { requestId }
  })
}

// 更新服务请求状态
export function updateStatus(data) {
  return request({
    url: '/service/request/updateStatus',
    method: 'post',
    data: data
  })
}

// 更新服务请求详情
export function updateServiceDetails(data) {
  return request({
    url: '/service/request/updateDetails',
    method: 'post',
    data: data
  })
}

// 删除服务请求
export function deleteServiceRequest(requestId) {
  return request({
    url: '/service/request/delete',
    method: 'get',
    params: { requestId }
  })
}

// 添加服务请求
export function addServiceRequest(data) {
  return request({
    url: '/service/request/add',
    method: 'post',
    data: data
  })
}

// 更新服务请求
export function updateServiceRequest(data) {
  return request({
    url: '/service/request/update',
    method: 'post',
    data: data
  })
}
