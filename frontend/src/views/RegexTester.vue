<template>
  <div class="regex-tester">
    <h2>正则表达式测试</h2>

    <!-- Quick patterns -->
    <div class="quick-patterns">
      <span class="label">常用正则：</span>
      <el-tag
        v-for="item in quickPatterns"
        :key="item.name"
        class="quick-tag"
        @click="applyPattern(item)"
      >{{ item.name }}</el-tag>
    </div>

    <!-- Regex input -->
    <el-row :gutter="16" class="regex-row">
      <el-col :span="18">
        <div class="panel-label">正则表达式</div>
        <el-input v-model="pattern" placeholder="输入正则表达式，例如: \d+" clearable />
      </el-col>
      <el-col :span="6">
        <div class="panel-label">标志</div>
        <el-checkbox-group v-model="flags">
          <el-checkbox label="g" />
          <el-checkbox label="i" />
          <el-checkbox label="m" />
          <el-checkbox label="s" />
        </el-checkbox-group>
      </el-col>
    </el-row>

    <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>

    <!-- Test text & results -->
    <el-row :gutter="16">
      <el-col :span="12">
        <div class="panel-label">测试文本</div>
        <el-input
          v-model="testText"
          type="textarea"
          :rows="16"
          placeholder="输入待匹配的文本..."
        />
      </el-col>
      <el-col :span="12">
        <div class="panel-label">匹配预览</div>
        <div class="preview-box" v-html="highlightedHtml"></div>
      </el-col>
    </el-row>

    <!-- Match list -->
    <div v-if="matches.length" class="match-section">
      <div class="panel-label">
        匹配结果（共 {{ matches.length }} 个）
      </div>
      <el-table :data="matches" border stripe size="small" max-height="300">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="value" label="匹配内容" />
        <el-table-column prop="index" label="起始位置" width="100" />
        <el-table-column prop="length" label="长度" width="80" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const quickPatterns = [
  { name: '邮箱', pattern: '[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}' },
  { name: '手机号', pattern: '1[3-9]\\d{9}' },
  { name: 'URL', pattern: 'https?://[\\w\\-._~:/?#\\[\\]@!$&\'()*+,;=%]+' },
  { name: 'IP地址', pattern: '(?:\\d{1,3}\\.){3}\\d{1,3}' },
  { name: '身份证号', pattern: '\\d{17}[\\dXx]' },
  { name: '日期', pattern: '\\d{4}[-/]\\d{1,2}[-/]\\d{1,2}' },
  { name: '中文字符', pattern: '[\\u4e00-\\u9fa5]+' },
  { name: 'HTML标签', pattern: '<[^>]+>' },
]

const pattern = ref('')
const testText = ref('')
const flags = ref(['g'])

const flagStr = computed(() => flags.value.join(''))

const regex = computed(() => {
  if (!pattern.value) return null
  try {
    return new RegExp(pattern.value, flagStr.value)
  } catch {
    return null
  }
})

const errorMsg = computed(() => {
  if (!pattern.value) return ''
  try {
    new RegExp(pattern.value, flagStr.value)
    return ''
  } catch (e) {
    return e.message
  }
})

const matches = computed(() => {
  if (!regex.value || !testText.value) return []
  const result = []
  if (flags.value.includes('g')) {
    let m
    const re = new RegExp(pattern.value, flagStr.value)
    while ((m = re.exec(testText.value)) !== null) {
      result.push({ value: m[0], index: m.index, length: m[0].length })
      if (!m[0]) break // prevent infinite loop on zero-length match
    }
  } else {
    const m = regex.value.exec(testText.value)
    if (m) {
      result.push({ value: m[0], index: m.index, length: m[0].length })
    }
  }
  return result
})

const highlightedHtml = computed(() => {
  if (!testText.value) return '<span class="placeholder">匹配预览...</span>'
  if (!regex.value || !matches.value.length) return escapeHtml(testText.value)

  let html = ''
  let lastIdx = 0
  for (const m of matches.value) {
    html += escapeHtml(testText.value.slice(lastIdx, m.index))
    html += `<mark>${escapeHtml(m.value)}</mark>`
    lastIdx = m.index + m.length
  }
  html += escapeHtml(testText.value.slice(lastIdx))
  return html
})

function escapeHtml(str) {
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
}

function applyPattern(item) {
  pattern.value = item.pattern
}
</script>

<style scoped>
.regex-tester {
  padding: 20px;
}

.quick-patterns {
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.quick-patterns .label {
  font-weight: 500;
  color: #606266;
  flex-shrink: 0;
}

.quick-tag {
  cursor: pointer;
}

.regex-row {
  margin-bottom: 12px;
}

.panel-label {
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
}

.error-msg {
  margin-bottom: 12px;
  padding: 8px 12px;
  background: #fef0f0;
  color: #f56c6c;
  border-radius: 4px;
  font-size: 13px;
}

.preview-box {
  height: 100%;
  min-height: 336px;
  padding: 12px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  background: #fafafa;
  white-space: pre-wrap;
  word-break: break-all;
  font-size: 14px;
  line-height: 1.6;
  overflow: auto;
}

.preview-box :deep(mark) {
  background: #fadb14;
  color: #333;
  padding: 1px 2px;
  border-radius: 2px;
}

.preview-box :deep(.placeholder) {
  color: #c0c4cc;
}

.match-section {
  margin-top: 16px;
}
</style>
