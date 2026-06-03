import request, { downloadFile } from './request'

/** Excel - 下载模板 */
export function downloadTemplate() {
  return downloadFile('/excel/template', null, '员工信息导入模板.xlsx')
}

/** Excel - 导出数据 */
export function exportExcel() {
  return downloadFile('/excel/export', null, '员工信息导出.xlsx')
}

/** Excel - 导入 */
export function importExcel(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/excel/import', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
