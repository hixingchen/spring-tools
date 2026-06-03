<template>
  <div class="timestamp-converter">
    <h2>时间戳转换</h2>

    <!-- 当前时间戳实时显示 -->
    <el-card shadow="never" class="section">
      <template #header>
        <div class="card-header">当前时间戳</div>
      </template>
      <el-row :gutter="16" align="middle">
        <el-col :span="10">
          <div class="label">秒级</div>
          <div class="current-ts">
            <span class="ts-value">{{ currentSecond }}</span>
            <el-button link type="primary" @click="copyText(currentSecond)">复制</el-button>
          </div>
        </el-col>
        <el-col :span="10">
          <div class="label">毫秒级</div>
          <div class="current-ts">
            <span class="ts-value">{{ currentMillisecond }}</span>
            <el-button link type="primary" @click="copyText(currentMillisecond)">复制</el-button>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="label">&nbsp;</div>
          <div class="current-datetime">{{ currentDateTime }}</div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 时间戳转日期 -->
    <el-card shadow="never" class="section">
      <template #header>
        <div class="card-header">时间戳转日期</div>
      </template>
      <el-row :gutter="16" align="middle">
        <el-col :span="10">
          <el-input
            v-model="tsInput"
            placeholder="输入时间戳"
            clearable
            @keyup.enter="tsToDate"
          >
            <template #prepend>时间戳</template>
          </el-input>
        </el-col>
        <el-col :span="5">
          <el-select v-model="tsUnit" style="width: 100%">
            <el-option label="秒 (s)" value="s" />
            <el-option label="毫秒 (ms)" value="ms" />
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" @click="tsToDate">转换</el-button>
        </el-col>
        <el-col :span="6">
          <el-input v-model="tsToDateResult" readonly placeholder="转换结果">
            <template #append>
              <el-button @click="copyText(tsToDateResult)">复制</el-button>
            </template>
          </el-input>
        </el-col>
      </el-row>
    </el-card>

    <!-- 日期转时间戳 -->
    <el-card shadow="never" class="section">
      <template #header>
        <div class="card-header">日期转时间戳</div>
      </template>
      <el-row :gutter="16" align="middle">
        <el-col :span="10">
          <el-date-picker
            v-model="dateInput"
            type="datetime"
            placeholder="选择日期时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-col>
        <el-col :span="3">
          <el-button type="primary" @click="dateToTs">转换</el-button>
        </el-col>
        <el-col :span="5">
          <div class="result-row">
            <span class="result-label">秒:</span>
            <el-input v-model="dateToTsSecond" readonly>
              <template #append>
                <el-button @click="copyText(dateToTsSecond)">复制</el-button>
              </template>
            </el-input>
          </div>
        </el-col>
        <el-col :span="5">
          <div class="result-row">
            <span class="result-label">毫秒:</span>
            <el-input v-model="dateToTsMillisecond" readonly>
              <template #append>
                <el-button @click="copyText(dateToTsMillisecond)">复制</el-button>
              </template>
            </el-input>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'

// 当前时间戳
const now = ref(Date.now())
let timer = null

onMounted(() => {
  timer = setInterval(() => {
    now.value = Date.now()
  }, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
})

const currentSecond = computed(() => Math.floor(now.value / 1000))
const currentMillisecond = computed(() => now.value)
const currentDateTime = computed(() => formatTimestamp(now.value))

// 时间戳转日期
const tsInput = ref('')
const tsUnit = ref('s')
const tsToDateResult = ref('')

function tsToDate() {
  if (!tsInput.value) {
    ElMessage.warning('请输入时间戳')
    return
  }
  const num = Number(tsInput.value)
  if (isNaN(num)) {
    ElMessage.error('请输入有效的数字')
    return
  }
  const ms = tsUnit.value === 's' ? num * 1000 : num
  tsToDateResult.value = formatTimestamp(ms)
}

// 日期转时间戳
const dateInput = ref('')
const dateToTsSecond = ref('')
const dateToTsMillisecond = ref('')

function dateToTs() {
  if (!dateInput.value) {
    ElMessage.warning('请选择日期时间')
    return
  }
  const ms = new Date(dateInput.value).getTime()
  dateToTsSecond.value = String(Math.floor(ms / 1000))
  dateToTsMillisecond.value = String(ms)
}

// 工具函数
function formatTimestamp(ms) {
  const d = new Date(ms)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

function copyText(text) {
  if (!text) {
    ElMessage.warning('没有可复制的内容')
    return
  }
  navigator.clipboard.writeText(text)
  ElMessage.success('已复制到剪贴板')
}
</script>

<style scoped>
.timestamp-converter {
  padding: 20px;
}

.section {
  margin-bottom: 16px;
}

.card-header {
  font-weight: 500;
}

.label {
  margin-bottom: 4px;
  font-size: 13px;
  color: #909399;
}

.current-ts {
  display: flex;
  align-items: center;
  gap: 8px;
}

.ts-value {
  font-size: 20px;
  font-family: monospace;
  color: #303133;
  font-weight: 600;
}

.current-datetime {
  font-size: 13px;
  color: #606266;
}

.result-row {
  display: flex;
  align-items: center;
  gap: 6px;
}

.result-label {
  font-size: 13px;
  color: #606266;
  white-space: nowrap;
}
</style>
