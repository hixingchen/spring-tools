import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截器 — 自动带上 Token
request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器 — 统一处理错误
request.interceptors.response.use(
  response => {
    const res = response.data

    // 二进制数据直接返回（文件下载）
    if (response.config.responseType === 'blob') {
      return response
    }

    // 业务状态码判断
    if (res.code === 200) {
      return res.data
    }

    // 登录过期
    if (res.code === 401) {
      const userStore = useUserStore()
      userStore.logout()
      ElMessage.error('登录已过期，请重新登录')
      return Promise.reject(new Error(res.message))
    }

    // 其他业务错误
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message))
  },
  error => {
    // 网络错误
    const msg = error.response?.data?.message || error.message || '网络异常'
    ElMessage.error(msg)
    return Promise.reject(error)
  }
)

/**
 * 文件下载工具方法
 */
export function downloadFile(url, params, filename) {
  return request({ url, params, responseType: 'blob' }).then(response => {
    const blob = new Blob([response.data])
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = filename || decodeURIComponent(
      response.headers['content-disposition']?.split('filename=')[1] || 'download'
    )
    link.click()
    URL.revokeObjectURL(link.href)
  })
}

export default request
