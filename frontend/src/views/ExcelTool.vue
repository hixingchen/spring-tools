<template>
  <div class="excel-tool">
    <el-card>
      <template #header>
        <span>Excel 导入导出工具</span>
      </template>

      <!-- 导出区域 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <h4>导出数据</h4>
          <p>导出员工信息 Excel，包含下拉选择</p>
          <el-button type="primary" @click="exportExcel">导出 Excel</el-button>
          <el-button @click="downloadTemplate">下载导入模板</el-button>
        </el-col>

        <el-col :span="12">
          <h4>导入数据</h4>
          <el-upload
            action="/api/excel/import"
            :on-success="handleImportSuccess"
            :on-error="handleImportError"
            :before-upload="beforeUpload"
            accept=".xlsx,.xls"
            :show-file-list="false"
          >
            <el-button type="success">选择文件导入</el-button>
          </el-upload>

          <!-- 导入结果 -->
          <div v-if="importResult" class="import-result">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="导入总数">{{ importResult.total }}</el-descriptions-item>
              <el-descriptions-item label="成功数">{{ importResult.success }}</el-descriptions-item>
              <el-descriptions-item label="失败数">{{ importResult.fail }}</el-descriptions-item>
              <el-descriptions-item label="失败行">{{ importResult.failRows?.join(', ') || '无' }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { downloadFile } from '../api/index.js'

const importResult = ref(null)

function exportExcel() {
  downloadFile('/api/excel/export').catch(() => {
    ElMessage.error('导出失败')
  })
}

function downloadTemplate() {
  downloadFile('/api/excel/template').catch(() => {
    ElMessage.error('下载模板失败')
  })
}

function beforeUpload(file) {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    || file.type === 'application/vnd.ms-excel'
    || file.name.endsWith('.xlsx')
    || file.name.endsWith('.xls')
  if (!isExcel) {
    ElMessage.error('只能上传 Excel 文件')
    return false
  }
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 10MB')
    return false
  }
  return true
}

function handleImportSuccess(response) {
  if (response.code === 200) {
    importResult.value = response.data
    ElMessage.success('导入完成')
  } else {
    ElMessage.error(response.message || '导入失败')
  }
}

function handleImportError() {
  ElMessage.error('导入请求失败')
}
</script>

<style scoped>
.excel-tool {
  padding: 20px;
}

h4 {
  margin: 0 0 8px;
}

p {
  margin: 0 0 16px;
  color: #909399;
  font-size: 14px;
}

.import-result {
  margin-top: 20px;
}
</style>
