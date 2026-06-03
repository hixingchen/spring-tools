<template>
  <div class="base64-tool">
    <h2>Base64 编解码</h2>
    <el-row :gutter="16">
      <el-col :span="12">
        <div class="panel-label">输入</div>
        <el-input
          v-model="input"
          type="textarea"
          :rows="20"
          placeholder="输入待编码/解码的文本..."
        />
        <div class="actions">
          <el-button type="primary" @click="encode">编码</el-button>
          <el-button type="success" @click="decode">解码</el-button>
          <el-button @click="clearAll">清空</el-button>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="panel-label">输出</div>
        <el-input
          v-model="output"
          type="textarea"
          :rows="20"
          readonly
          placeholder="结果..."
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

function encode() {
  if (!input.value.trim()) {
    ElMessage.warning('请输入待编码的文本')
    return
  }
  try {
    output.value = btoa(unescape(encodeURIComponent(input.value)))
  } catch (e) {
    ElMessage.error('编码失败: ' + e.message)
  }
}

function decode() {
  if (!input.value.trim()) {
    ElMessage.warning('请输入待解码的 Base64 文本')
    return
  }
  try {
    output.value = decodeURIComponent(escape(atob(input.value.trim())))
  } catch (e) {
    ElMessage.error('解码失败: 输入不是有效的 Base64 字符串')
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

function clearAll() {
  input.value = ''
  output.value = ''
}
</script>

<style scoped>
.base64-tool {
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
