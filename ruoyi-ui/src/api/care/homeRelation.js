import request from '@/utils/request'

// 获取所有家庭关系
export function listAllHomeRelations(query) {
  return request({
    url: '/care/relation/listAll',
    method: 'get',
    params: query
  })
}

// 获取家庭关系详情
export function getHomeRelationById(relationId) {
  return request({
    url: '/care/relation/detail',
    method: 'get',
    params: { relationId }
  })
}

// 添加家庭关系
export function addHomeRelation(data) {
  return request({
    url: '/care/relation/add',
    method: 'post',
    data: data
  })
}

// 更新家庭关系
export function updateHomeRelation(data) {
  return request({
    url: '/care/relation/update',
    method: 'post',
    data: data
  })
}

// 删除家庭关系
export function deleteHomeRelation(relationId) {
  return request({
    url: '/care/relation/delete',
    method: 'get',
    params: { relationId }
  })
}
