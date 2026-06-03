import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.response.use(
  response => response.data,
  error => {
    ElMessage.error(error.response?.data?.message || '请求失败')
    return Promise.reject(error)
  }
)

export function downloadFile(url, params) {
  return axios({
    url,
    params,
    responseType: 'blob'
  }).then(response => {
    const blob = new Blob([response.data])
    const link = document.createElement('a')
    link.href = URL.createObjectURL(blob)
    link.download = decodeURIComponent(response.headers['content-disposition']?.split('filename=')[1] || 'download.xlsx')
    link.click()
    URL.revokeObjectURL(link.href)
  })
}

export default request
