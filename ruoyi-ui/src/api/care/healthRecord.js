import request from '@/utils/request'

// 查询所有健康记录
export function listAllHealthRecords(query) {
  return request({
    url: '/health/record/listAll',
    method: 'get',
    params: query
  })
}

// 查询健康记录详情
export function getHealthRecordById(recordId) {
  return request({
    url: '/health/record/detail',
    method: 'get',
    params: { recordId }
  })
}

// 添加健康记录
export function addHealthRecord(data) {
  return request({
    url: '/health/record/add',
    method: 'post',
    data: data
  })
}

// 更新健康记录
export function updateHealthRecord(data) {
  return request({
    url: '/health/record/update',
    method: 'post',
    data: data
  })
}

// 删除健康记录
export function deleteHealthRecord(recordId) {
  return request({
    url: '/health/record/delete',
    method: 'get',
    params: { recordId }
  })
}

export function countHighTemperatureToday(){
  return request({
    url: '/health/record/countHighTemperatureToday',
    method: 'get'
  })
}

export function countHighBloodSugarToday(){
  return request({
    url: '/health/record/countHighBloodSugarToday',
    method: 'get'
  })
}

export function countLowBloodSugarToday(){
  return request({
    url: '/health/record/countLowBloodSugarToday',
    method: 'get'
  })
}
