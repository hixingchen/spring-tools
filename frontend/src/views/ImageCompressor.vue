<template>
  <div class="image-compressor">
    <h2>图片压缩工具</h2>

    <!-- Upload Area -->
    <el-upload
      v-if="!originalSrc"
      class="upload-area"
      drag
      :auto-upload="false"
      :show-file-list="false"
      accept="image/*"
      @change="handleFileChange"
    >
      <div class="el-upload__text">拖拽图片到此处，或 <em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip">支持 JPG / PNG / WebP / BMP 格式</div>
      </template>
    </el-upload>

    <!-- Compression Controls & Preview -->
    <template v-if="originalSrc">
      <el-card class="compress-controls" shadow="never">
        <el-row :gutter="16" align="middle">
          <el-col :span="2">压缩质量</el-col>
          <el-col :span="16">
            <el-slider v-model="quality" :min="10" :max="100" :step="5" />
          </el-col>
          <el-col :span="6" class="quality-value">{{ quality }}%</el-col>
        </el-row>
        <el-row :gutter="16" align="middle" style="margin-top: 12px;">
          <el-col :span="2">输出格式</el-col>
          <el-col :span="8">
            <el-radio-group v-model="outputType">
              <el-radio value="image/jpeg">JPG</el-radio>
              <el-radio value="image/png">PNG</el-radio>
              <el-radio value="image/webp">WebP</el-radio>
            </el-radio-group>
          </el-col>
          <el-col :span="14" style="text-align: right;">
            <el-button type="primary" @click="compress">压缩图片</el-button>
            <el-button @click="reset">重新选择</el-button>
          </el-col>
        </el-row>
      </el-card>

      <el-row :gutter="20" class="preview-area">
        <el-col :span="12">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <span>原图</span>
                <el-tag type="info">{{ originalSizeText }}</el-tag>
              </div>
            </template>
            <div class="image-wrapper">
              <img :src="originalSrc" alt="原图预览" />
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover">
            <template #header>
              <div class="card-header">
                <span>压缩后</span>
                <template v-if="compressedSrc">
                  <el-tag :type="sizeSavedPercent > 0 ? 'success' : 'warning'">
                    {{ compressedSizeText }}
                    <template v-if="sizeSavedPercent > 0"> (节省 {{ sizeSavedPercent }}%)</template>
                  </el-tag>
                </template>
                <el-tag v-else type="info">待压缩</el-tag>
              </div>
            </template>
            <div class="image-wrapper">
              <img v-if="compressedSrc" :src="compressedSrc" alt="压缩后预览" />
              <el-empty v-else description="点击「压缩图片」查看效果" />
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div v-if="compressedSrc" class="download-area">
        <el-button type="success" size="large" @click="download">下载压缩图片</el-button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

const originalSrc = ref('')
const compressedSrc = ref('')
const originalSize = ref(0)
const compressedSize = ref(0)
const quality = ref(70)
const outputType = ref('image/jpeg')
const fileName = ref('')

// Hidden image element for canvas draw
let imgEl = null

function formatSize(bytes) {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(2) + ' MB'
}

const originalSizeText = computed(() => formatSize(originalSize.value))
const compressedSizeText = computed(() => formatSize(compressedSize.value))
const sizeSavedPercent = computed(() => {
  if (compressedSize.value <= 0 || compressedSize.value >= originalSize.value) return 0
  return Math.round(((originalSize.value - compressedSize.value) / originalSize.value) * 100)
})

function handleFileChange(file) {
  // file is from el-upload change event, it wraps the native File
  const rawFile = file.raw || file
  if (!rawFile || !rawFile.type || !rawFile.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }
  // Reset previous
  compressedSrc.value = ''
  compressedSize.value = 0
  fileName.value = rawFile.name

  originalSize.value = rawFile.size
  const reader = new FileReader()
  reader.onload = (e) => {
    originalSrc.value = e.target.result
    // Preload image for later canvas use
    imgEl = new Image()
    imgEl.src = e.target.result
  }
  reader.readAsDataURL(rawFile)
}

function compress() {
  if (!imgEl || !imgEl.complete) {
    ElMessage.warning('图片加载中，请稍候')
    return
  }

  const canvas = document.createElement('canvas')
  canvas.width = imgEl.naturalWidth
  canvas.height = imgEl.naturalHeight
  const ctx = canvas.getContext('2d')
  ctx.drawImage(imgEl, 0, 0)

  const mime = outputType.value
  const q = quality.value / 100

  canvas.toBlob(
    (blob) => {
      if (!blob) {
        ElMessage.error('压缩失败，请尝试其他格式或质量')
        return
      }
      compressedSize.value = blob.size
      // Revoke previous URL to avoid memory leak
      if (compressedSrc.value) URL.revokeObjectURL(compressedSrc.value)
      compressedSrc.value = URL.createObjectURL(blob)
      ElMessage.success('压缩完成')
    },
    mime,
    q
  )
}

function download() {
  if (!compressedSrc.value) return
  const extMap = { 'image/jpeg': 'jpg', 'image/png': 'png', 'image/webp': 'webp' }
  const ext = extMap[outputType.value] || 'jpg'
  // Build output filename from original name
  const baseName = fileName.value ? fileName.value.replace(/\.[^.]+$/, '') : 'image'
  const a = document.createElement('a')
  a.href = compressedSrc.value
  a.download = baseName + '_compressed.' + ext
  a.click()
}

function reset() {
  originalSrc.value = ''
  compressedSrc.value = ''
  originalSize.value = 0
  compressedSize.value = 0
  fileName.value = ''
  imgEl = null
}
</script>

<style scoped>
.image-compressor {
  padding: 20px;
}

.upload-area {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.compress-controls {
  margin-bottom: 20px;
}

.quality-value {
  font-size: 16px;
  font-weight: 600;
  color: #409eff;
}

.preview-area {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.image-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  max-height: 500px;
  overflow: auto;
}

.image-wrapper img {
  max-width: 100%;
  max-height: 480px;
  object-fit: contain;
}

.download-area {
  text-align: center;
}
</style>
