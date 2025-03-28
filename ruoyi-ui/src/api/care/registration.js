import request from '@/utils/request'

// 查询所有活动报名信息
export function listAllRegistrations(query) {
  return request({
    url: '/care/registration/listAll',
    method: 'get',
    params: query
  })
}

// 根据报名ID查询报名详情
export function getRegistrationById(registrationId) {
  return request({
    url: '/care/registration/get',
    method: 'get',
    params: { registrationId }
  })
}

// 添加活动报名信息
export function addRegistration(data) {
  return request({
    url: '/care/registration/add',
    method: 'post',
    data: data
  })
}

// 更新活动报名信息
export function updateRegistration(data) {
  return request({
    url: '/care/registration/update',
    method: 'post',
    data: data
  })
}

// 删除活动报名信息
export function deleteRegistration(registrationId) {
  return request({
    url: '/care/registration/delete',
    method: 'get',
    params: { registrationId }
  })
}

// 根据活动ID查询报名列表
export function listByActivityId(activityId) {
  return request({
    url: '/care/registration/listByActivityId',
    method: 'get',
    params: { activityId }
  })
}

// 根据用户ID查询报名列表
export function listByUserId(userId) {
  return request({
    url: '/care/registration/listByUserId',
    method: 'get',
    params: { userId }
  })
}
