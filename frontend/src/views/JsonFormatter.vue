<template>
  <div class="json-formatter">
    <h2>JSON 格式化</h2>
    <el-row :gutter="16">
      <el-col :span="12">
        <div class="panel-label">输入</div>
        <el-input
          v-model="input"
          type="textarea"
          :rows="20"
          placeholder="粘贴 JSON 数据..."
        />
        <div class="actions">
          <el-button type="primary" @click="format">格式化</el-button>
          <el-button @click="input = ''">清空</el-button>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="panel-label">输出</div>
        <el-input
          v-model="output"
          type="textarea"
          :rows="20"
          readonly
          placeholder="格式化结果..."
        />
        <div class="actions">
          <el-button @click="copy">复制</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const input = ref('')
const output = ref('')

function format() {
  if (!input.value.trim()) {
    ElMessage.warning('请输入 JSON 数据')
    return
  }
  try {
    const parsed = JSON.parse(input.value)
    output.value = JSON.stringify(parsed, null, 2)
  } catch (e) {
    ElMessage.error('JSON 格式错误: ' + e.message)
  }
}

function copy() {
  if (!output.value) {
    ElMessage.warning('没有可复制的内容')
    return
  }
  navigator.clipboard.writeText(output.value)
  ElMessage.success('已复制到剪贴板')
}
</script>

<style scoped>
.json-formatter {
  padding: 20px;
}

.panel-label {
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
}

.actions {
  margin-top: 12px;
}
</style>
