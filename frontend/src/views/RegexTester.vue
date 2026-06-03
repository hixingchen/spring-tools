<template>
  <div class="regex-tester">
    <PageHeader :icon="Search" title="正则表达式测试" description="在线测试和调试正则表达式" />

    <div class="main-content">
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
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Search } from '@element-plus/icons-vue'
import PageHeader from '@/components/PageHeader.vue'

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
  min-height: 100vh;
  background: #f0f2f5;
  display: flex;
  flex-direction: column;
}

p, span {
  color: rgba(255, 255, 255, 0.8);
}

.main-content {
  padding: 24px 40px;
  flex: 1;
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
  color: rgba(255, 255, 255, 0.8);
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
  color: rgba(255, 255, 255, 0.8);
}

.error-msg {
  margin-bottom: 12px;
  padding: 8px 12px;
  background: rgba(245, 108, 108, 0.15);
  color: #f56c6c;
  border: 1px solid rgba(245, 108, 108, 0.3);
  border-radius: 4px;
  font-size: 13px;
}

.preview-box {
  height: 100%;
  min-height: 336px;
  padding: 12px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.05);
  white-space: pre-wrap;
  word-break: break-all;
  font-size: 14px;
  line-height: 1.6;
  overflow: auto;
  color: rgba(255, 255, 255, 0.85);
}

.preview-box :deep(mark) {
  background: rgba(250, 219, 20, 0.3);
  color: #fadb14;
  padding: 1px 2px;
  border-radius: 2px;
}

.preview-box :deep(.placeholder) {
  color: rgba(255, 255, 255, 0.3);
}

.match-section {
  margin-top: 16px;
}

:deep(.el-input__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
}

:deep(.el-input__inner:focus) {
  border-color: #409eff;
}

:deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
}

:deep(.el-textarea__inner:focus) {
  border-color: #409eff;
}

:deep(.el-checkbox__label) {
  color: rgba(255, 255, 255, 0.8);
}

:deep(.el-table) {
  background: transparent;
  color: #fff;
}

:deep(.el-table th) {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-table td) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-table tr) {
  background: transparent;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell) {
  background: rgba(255, 255, 255, 0.03);
}
</style>
