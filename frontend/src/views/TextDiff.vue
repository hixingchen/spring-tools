<template>
  <div class="text-diff">
    <PageHeader :icon="Notebook" title="文本对比" description="逐行对比两段文本的差异" />

    <div class="main-content">
    <el-row :gutter="16">
      <el-col :span="12">
        <div class="panel-label">原始文本</div>
        <el-input
          v-model="leftText"
          type="textarea"
          :rows="20"
          placeholder="请输入原始文本..."
        />
      </el-col>
      <el-col :span="12">
        <div class="panel-label">对比文本</div>
        <el-input
          v-model="rightText"
          type="textarea"
          :rows="20"
          placeholder="请输入对比文本..."
        />
      </el-col>
    </el-row>

    <div class="actions">
      <el-button type="primary" @click="compare">对比</el-button>
      <el-button @click="clear">清空</el-button>
    </div>

    <template v-if="diffResult">
      <div class="stats">
        <el-tag type="success">新增 {{ stats.added }} 行</el-tag>
        <el-tag type="danger">删除 {{ stats.removed }} 行</el-tag>
        <el-tag type="warning">相同 {{ stats.same }} 行</el-tag>
      </div>

      <div class="diff-output">
        <div class="diff-line" v-for="(line, idx) in diffResult" :key="idx" :class="line.type">
          <span class="line-num left">{{ line.leftNum }}</span>
          <span class="line-num right">{{ line.rightNum }}</span>
          <span class="line-prefix">{{ line.prefix }}</span>
          <span class="line-content">{{ line.text }}</span>
        </div>
      </div>
    </template>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Notebook } from '@element-plus/icons-vue'
import PageHeader from '@/components/PageHeader.vue'

const leftText = ref('')
const rightText = ref('')
const diffResult = ref(null)
const stats = reactive({ added: 0, removed: 0, same: 0 })

function computeLcs(a, b) {
  const m = a.length
  const n = b.length
  const dp = Array.from({ length: m + 1 }, () => new Array(n + 1).fill(0))

  for (let i = 1; i <= m; i++) {
    for (let j = 1; j <= n; j++) {
      dp[i][j] = a[i - 1] === b[j - 1] ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1])
    }
  }

  const lcs = []
  let i = m, j = n
  while (i > 0 && j > 0) {
    if (a[i - 1] === b[j - 1]) {
      lcs.unshift({ line: a[i - 1], leftIdx: i - 1, rightIdx: j - 1 })
      i--
      j--
    } else if (dp[i - 1][j] > dp[i][j - 1]) {
      i--
    } else {
      j--
    }
  }
  return lcs
}

function compare() {
  if (!leftText.value.trim() && !rightText.value.trim()) {
    ElMessage.warning('请输入文本')
    return
  }

  const leftLines = leftText.value.split('\n')
  const rightLines = rightText.value.split('\n')
  const lcs = computeLcs(leftLines, rightLines)

  const result = []
  let li = 0, ri = 0, lcsIdx = 0

  stats.added = 0
  stats.removed = 0
  stats.same = 0

  while (li < leftLines.length || ri < rightLines.length) {
    if (lcsIdx < lcs.length && li === lcs[lcsIdx].leftIdx && ri === lcs[lcsIdx].rightIdx) {
      result.push({ type: 'same', prefix: ' ', text: leftLines[li], leftNum: li + 1, rightNum: ri + 1 })
      stats.same++
      li++
      ri++
      lcsIdx++
    } else {
      if (lcsIdx < lcs.length && li < lcs[lcsIdx].leftIdx) {
        result.push({ type: 'removed', prefix: '-', text: leftLines[li], leftNum: li + 1, rightNum: '' })
        stats.removed++
        li++
      }
      if (lcsIdx < lcs.length && ri < lcs[lcsIdx].rightIdx) {
        result.push({ type: 'added', prefix: '+', text: rightLines[ri], leftNum: '', rightNum: ri + 1 })
        stats.added++
        ri++
      }
      if (lcsIdx >= lcs.length) {
        if (li < leftLines.length) {
          result.push({ type: 'removed', prefix: '-', text: leftLines[li], leftNum: li + 1, rightNum: '' })
          stats.removed++
          li++
        }
        if (ri < rightLines.length) {
          result.push({ type: 'added', prefix: '+', text: rightLines[ri], leftNum: '', rightNum: ri + 1 })
          stats.added++
          ri++
        }
      }
    }
  }

  diffResult.value = result
}

function clear() {
  leftText.value = ''
  rightText.value = ''
  diffResult.value = null
  stats.added = 0
  stats.removed = 0
  stats.same = 0
}
</script>

<style scoped>
.text-diff {
  min-height: 100vh;
  background: #f0f2f5;
  display: flex;
  flex-direction: column;
}

.main-content {
  padding: 24px 40px;
  flex: 1;
}

.panel-label {
  margin-bottom: 8px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.8);
}

.actions {
  margin-top: 12px;
  margin-bottom: 16px;
}

.stats {
  margin-bottom: 12px;
  display: flex;
  gap: 8px;
}

:deep(.el-textarea__inner) {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
}

:deep(.el-textarea__inner:focus) {
  border-color: #409eff;
}

.diff-output {
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  line-height: 1.6;
  overflow-x: auto;
}

.diff-line {
  display: flex;
  padding: 1px 8px;
  white-space: pre-wrap;
  word-break: break-all;
}

.diff-line.same {
  background-color: rgba(255, 255, 255, 0.03);
}

.diff-line.added {
  background-color: rgba(103, 194, 58, 0.2);
  color: #67c23a;
}

.diff-line.removed {
  background-color: rgba(245, 108, 108, 0.2);
  color: #f56c6c;
}

.line-num {
  display: inline-block;
  width: 36px;
  text-align: right;
  color: rgba(255, 255, 255, 0.4);
  user-select: none;
  flex-shrink: 0;
  margin-right: 8px;
}

.line-prefix {
  display: inline-block;
  width: 16px;
  flex-shrink: 0;
  color: rgba(255, 255, 255, 0.5);
  user-select: none;
}

.line-content {
  flex: 1;
  color: rgba(255, 255, 255, 0.85);
}
</style>
