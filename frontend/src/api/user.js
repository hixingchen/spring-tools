import request from './request'

/** 登录 */
export function login(data) {
  return request.post('/user/login', data)
}

/** 注册 */
export function register(data) {
  return request.post('/user/register', data)
}

/** 获取当前用户信息 */
export function getUserInfo() {
  return request.get('/user/info')
}

/** 退出登录 */
export function logout() {
  return request.post('/user/logout')
}
