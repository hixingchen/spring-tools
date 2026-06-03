<template>
  <div class="json-formatter">
    <PageHeader :icon="Document" title="JSON 格式化" description="格式化、压缩、校验 JSON 数据" />

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" @click="format" :icon="Check">
          格式化
        </el-button>
        <el-button @click="compress" :icon="ZoomOut">
          压缩
        </el-button>
        <el-button @click="validate" :icon="CircleCheck">
          校验
        </el-button>
        <el-divider direction="vertical" />
        <el-button @click="clear" :icon="Delete">
          清空
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-button @click="copy" :icon="CopyDocument" :disabled="!output">
          复制结果
        </el-button>
      </div>
    </div>

    <!-- 编辑区域 -->
    <div class="editor-container">
      <div class="editor-panel input-panel">
        <div class="panel-header">
          <span class="panel-title">
            <el-icon><Edit /></el-icon>
            输入
          </span>
          <span class="panel-info" v-if="input">
            {{ input.length }} 字符
          </span>
        </div>
        <div class="editor-wrapper">
          <textarea
            ref="inputRef"
            v-model="input"
            class="code-editor"
            placeholder="在此粘贴或输入 JSON 数据..."
            spellcheck="false"
            @input="onInput"
          ></textarea>
          <div class="line-numbers" v-if="input">
            <div v-for="n in lineCount" :key="n" class="line-number">{{ n }}</div>
          </div>
        </div>
      </div>

      <div class="editor-divider">
        <el-icon><ArrowRight /></el-icon>
      </div>

      <div class="editor-panel output-panel">
        <div class="panel-header">
          <span class="panel-title">
            <el-icon><View /></el-icon>
            输出
          </span>
          <span class="panel-info" v-if="output">
            {{ output.length }} 字符
          </span>
        </div>
        <div class="editor-wrapper">
          <pre class="code-output" v-if="output"><code>{{ output }}</code></pre>
          <div class="empty-state" v-else>
            <el-icon class="empty-icon"><Document /></el-icon>
            <p>格式化结果将显示在这里</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 状态栏 -->
    <div class="status-bar" v-if="statusMessage">
      <el-icon v-if="statusType === 'success'" class="status-icon success"><CircleCheck /></el-icon>
      <el-icon v-else-if="statusType === 'error'" class="status-icon error"><CircleClose /></el-icon>
      <el-icon v-else class="status-icon info"><InfoFilled /></el-icon>
      <span>{{ statusMessage }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import {
  Document, Check, ZoomOut, CircleCheck, Delete,
  CopyDocument, Edit, View, ArrowRight, CircleClose, InfoFilled
} from '@element-plus/icons-vue'
import PageHeader from '@/components/PageHeader.vue'

const input = ref('')
const output = ref('')
const statusMessage = ref('')
const statusType = ref('info')

const lineCount = computed(() => {
  if (!input.value) return 0
  return input.value.split('\n').length
})

function onInput() {
  statusMessage.value = ''
}

function format() {
  if (!input.value.trim()) {
    showStatus('请输入 JSON 数据', 'error')
    return
  }
  try {
    const parsed = JSON.parse(input.value)
    output.value = JSON.stringify(parsed, null, 2)
    showStatus('格式化成功', 'success')
  } catch (e) {
    showStatus('JSON 格式错误: ' + e.message, 'error')
  }
}

function compress() {
  if (!input.value.trim()) {
    showStatus('请输入 JSON 数据', 'error')
    return
  }
  try {
    const parsed = JSON.parse(input.value)
    output.value = JSON.stringify(parsed)
    showStatus('压缩成功', 'success')
  } catch (e) {
    showStatus('JSON 格式错误: ' + e.message, 'error')
  }
}

function validate() {
  if (!input.value.trim()) {
    showStatus('请输入 JSON 数据', 'error')
    return
  }
  try {
    JSON.parse(input.value)
    showStatus('JSON 格式正确 ✓', 'success')
  } catch (e) {
    showStatus('JSON 格式错误: ' + e.message, 'error')
  }
}

function clear() {
  input.value = ''
  output.value = ''
  statusMessage.value = ''
}

function copy() {
  if (!output.value) {
    showStatus('没有可复制的内容', 'error')
    return
  }
  navigator.clipboard.writeText(output.value)
  showStatus('已复制到剪贴板', 'success')
}

function showStatus(message, type) {
  statusMessage.value = message
  statusType.value = type
}
</script>

<style scoped>
.json-formatter {
  min-height: 100vh;
  background: #f0f2f5;
  display: flex;
  flex-direction: column;
}

/* 工具栏 */
.toolbar {
  background: #fff;
  padding: 16px 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 10;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 编辑区域 */
.editor-container {
  flex: 1;
  display: flex;
  padding: 24px 40px;
  gap: 24px;
  max-width: 100%;
  overflow: hidden;
}

.editor-panel {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fafafa;
}

.panel-title {
  font-weight: 500;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.panel-info {
  font-size: 12px;
  color: #999;
  background: #f0f0f0;
  padding: 4px 8px;
  border-radius: 4px;
}

.editor-wrapper {
  flex: 1;
  position: relative;
  overflow: auto;
}

.code-editor {
  width: 100%;
  height: 100%;
  min-height: 500px;
  padding: 20px;
  padding-left: 60px;
  border: none;
  outline: none;
  resize: none;
  font-family: 'JetBrains Mono', 'Fira Code', 'Consolas', monospace;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background: #fff;
  tab-size: 2;
}

.code-editor::placeholder {
  color: #bbb;
}

.line-numbers {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 48px;
  background: #f8f8f8;
  border-right: 1px solid #e8e8e8;
  padding: 20px 0;
  user-select: none;
  overflow: hidden;
}

.line-number {
  height: 22.4px;
  line-height: 22.4px;
  text-align: right;
  padding-right: 12px;
  font-family: 'JetBrains Mono', 'Fira Code', 'Consolas', monospace;
  font-size: 12px;
  color: #999;
}

.code-output {
  width: 100%;
  height: 100%;
  min-height: 500px;
  margin: 0;
  padding: 20px;
  font-family: 'JetBrains Mono', 'Fira Code', 'Consolas', monospace;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background: #fff;
  overflow: auto;
  white-space: pre;
  tab-size: 2;
}

.editor-divider {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ccc;
  font-size: 24px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  min-height: 500px;
  color: #ccc;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-state p {
  margin: 0;
  font-size: 14px;
}

/* 状态栏 */
.status-bar {
  background: #fff;
  padding: 12px 40px;
  display: flex;
  align-items: center;
  gap: 8px;
  border-top: 1px solid #e8e8e8;
  font-size: 14px;
  color: #666;
}

.status-icon {
  font-size: 16px;
}

.status-icon.success {
  color: #67c23a;
}

.status-icon.error {
  color: #f56c6c;
}

.status-icon.info {
  color: #409eff;
}

/* 响应式 */
@media (max-width: 1200px) {
  .editor-container {
    flex-direction: column;
    padding: 16px 20px;
  }

  .editor-divider {
    transform: rotate(90deg);
  }

  .code-editor,
  .code-output {
    min-height: 300px;
  }

  .empty-state {
    min-height: 300px;
  }
}

@media (max-width: 768px) {
  .page-header {
    padding: 24px 20px;
  }

  .page-title {
    font-size: 22px;
  }

  .toolbar {
    padding: 12px 20px;
    flex-direction: column;
    gap: 12px;
  }

  .toolbar-left,
  .toolbar-right {
    width: 100%;
    justify-content: center;
  }
}
</style>