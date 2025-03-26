import request from '@/utils/request'

// 查询所有社区活动
export function listAllCommunityActivities(query) {
  return request({
    url: '/community/activity/listAll',
    method: 'get',
    params: query
  })
}

// 查询社区活动详情
export function getCommunityActivityById(activityId) {
  return request({
    url: '/community/activity/detail',
    method: 'get',
    params: { activityId }
  })
}

// 添加社区活动
export function addCommunityActivity(data) {
  return request({
    url: '/community/activity/add',
    method: 'post',
    data: data
  })
}

// 更新社区活动
export function updateCommunityActivity(data) {
  return request({
    url: '/community/activity/update',
    method: 'post',
    data: data
  })
}

// 删除社区活动
export function deleteCommunityActivity(activityId) {
  return request({
    url: '/community/activity/delete',
    method: 'get',
    params: { activityId }
  })
}
