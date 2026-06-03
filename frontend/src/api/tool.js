import request from './request'

/** 获取工具列表 */
export function getToolList() {
  return request.get('/tool/list')
}

/** 获取工具详情 */
export function getToolDetail(id) {
  return request.get(`/tool/${id}`)
}
