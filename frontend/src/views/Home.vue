<template>
  <div class="home">
    <!-- 背景动画 -->
    <div class="bg-animation">
      <div v-for="i in 20" :key="i" class="particle" :style="particleStyles[i - 1]"></div>
    </div>

    <!-- 头部区域 -->
    <div class="header">
      <div class="header-content">
        <h1 class="title">
          <span class="title-text">开发工具箱</span>
          <span class="title-sub">Developer Toolbox</span>
        </h1>
        <div class="search-container">
          <el-input
            v-model="searchText"
            placeholder="搜索工具..."
            :prefix-icon="Search"
            clearable
            size="large"
            class="search-input"
          />
        </div>
        <div class="stats">
          <div class="stat-item">
            <div class="stat-number">{{ tools.length }}</div>
            <div class="stat-label">工具总数</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">5</div>
            <div class="stat-label">工具分类</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">100%</div>
            <div class="stat-label">免费开源</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 工具卡片 -->
    <div class="tools-section">
      <div class="section-title">
        <span class="line"></span>
        <span class="text">常用工具</span>
        <span class="line"></span>
      </div>

      <div v-if="filteredTools.length === 0" class="no-result">
        <el-empty description="没有找到匹配的工具" />
      </div>
      <div v-else class="tools-grid">
        <div
          v-for="tool in filteredTools"
          :key="tool.path"
          class="tool-card"
          @click="router.push(tool.path)"
        >
          <div class="card-content">
            <div class="card-icon" :style="{ background: tool.gradient }">
              <el-icon :size="28"><component :is="tool.icon" /></el-icon>
            </div>
            <h3 class="card-title">{{ tool.title }}</h3>
            <p class="card-desc">{{ tool.desc }}</p>
            <div class="card-tags">
              <span v-for="tag in tool.tags" :key="tag" class="tag">{{ tag }}</span>
            </div>
          </div>
          <div class="card-arrow">
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部信息 -->
    <div class="footer">
      <p>Powered by Vue 3 + Spring Boot | Made with ❤️</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  Document, Key, Clock, DocumentCopy, Picture,
  Search, Files, Monitor, Platform, ArrowRight
} from '@element-plus/icons-vue'

const router = useRouter()
const searchText = ref('')

const particleStyles = computed(() => {
  return Array.from({ length: 20 }, () => {
    const size = Math.random() * 6 + 2
    return {
      width: size + 'px',
      height: size + 'px',
      left: Math.random() * 100 + '%',
      top: Math.random() * 100 + '%',
      animationDuration: (Math.random() * 20 + 10) + 's',
      animationDelay: (Math.random() * 5) + 's'
    }
  })
})

const tools = [
  {
    path: '/json-formatter',
    title: 'JSON 格式化',
    desc: '格式化、压缩、校验 JSON 数据，支持语法高亮',
    icon: Document,
    gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    tags: ['格式化', '校验']
  },
  {
    path: '/base64-tool',
    title: 'Base64 编解码',
    desc: 'Base64 文本编码和解码，支持中文',
    icon: Key,
    gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    tags: ['编码', '解码']
  },
  {
    path: '/timestamp-converter',
    title: '时间戳转换',
    desc: '时间戳与日期互相转换，支持秒和毫秒',
    icon: Clock,
    gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    tags: ['时间', '转换']
  },
  {
    path: '/text-diff',
    title: '文本对比',
    desc: '对比两段文本的差异，高亮显示变更',
    icon: DocumentCopy,
    gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    tags: ['对比', '差异']
  },
  {
    path: '/image-compressor',
    title: '图片压缩',
    desc: '前端 Canvas 压缩图片，支持质量调节',
    icon: Picture,
    gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
    tags: ['压缩', '图片']
  },
  {
    path: '/regex-tester',
    title: '正则表达式测试',
    desc: '测试和调试正则表达式，实时匹配预览',
    icon: Search,
    gradient: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
    tags: ['正则', '测试']
  },
  {
    path: '/excel-tool',
    title: 'Excel 导入导出',
    desc: 'Excel 文件导入导出，支持下拉选择',
    icon: Files,
    gradient: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)',
    tags: ['Excel', '导入导出']
  },
  {
    path: '/linux-commands',
    title: 'Linux 命令',
    desc: 'Linux 常见命令速查手册',
    icon: Monitor,
    gradient: 'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)',
    tags: ['Linux', '命令']
  },
  {
    path: '/windows-commands',
    title: 'Windows 命令',
    desc: 'Windows 常见命令速查手册',
    icon: Platform,
    gradient: 'linear-gradient(135deg, #d4fc79 0%, #96e6a1 100%)',
    tags: ['Windows', '命令']
  }
]

const filteredTools = computed(() => {
  if (!searchText.value) return tools
  const keyword = searchText.value.toLowerCase()
  return tools.filter(tool =>
    tool.title.toLowerCase().includes(keyword) ||
    tool.desc.toLowerCase().includes(keyword) ||
    tool.tags.some(tag => tag.toLowerCase().includes(keyword))
  )
})
</script>

<style scoped>
.home {
  min-height: 100vh;
  background: linear-gradient(135deg, #0c0c1d 0%, #1a1a3e 50%, #0c0c1d 100%);
  position: relative;
  overflow: hidden;
  padding-bottom: 60px;
}

.bg-animation {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.particle {
  position: absolute;
  background: rgba(64, 158, 255, 0.6);
  border-radius: 50%;
  animation: float linear infinite;
}

@keyframes float {
  0% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) rotate(720deg);
    opacity: 0;
  }
}

.header {
  position: relative;
  z-index: 1;
  padding: 60px 40px 40px;
  text-align: center;
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
}

.title {
  margin-bottom: 40px;
}

.title-text {
  display: block;
  font-size: 48px;
  font-weight: 700;
  background: linear-gradient(90deg, #409eff, #53a8ff, #79bbff, #53a8ff, #409eff);
  background-size: 200% auto;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: shine 3s linear infinite;
}

.title-sub {
  display: block;
  font-size: 18px;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 10px;
  letter-spacing: 4px;
  text-transform: uppercase;
}

.search-container {
  margin: 30px auto;
  max-width: 500px;
}

.search-input .el-input__wrapper {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 24px;
  box-shadow: none;
}

.search-input .el-input__wrapper:hover {
  border-color: rgba(64, 158, 255, 0.5);
}

.search-input .el-input__wrapper.is-focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.search-input .el-input__inner {
  color: #fff;
}

.search-input .el-input__inner::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.search-input .el-input__prefix .el-icon {
  color: rgba(255, 255, 255, 0.5);
}

.no-result {
  text-align: center;
  padding: 60px 0;
}

@keyframes shine {
  to {
    background-position: 200% center;
  }
}

.stats {
  display: flex;
  justify-content: center;
  gap: 60px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 36px;
  font-weight: 700;
  color: #409eff;
  text-shadow: 0 0 20px rgba(64, 158, 255, 0.5);
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 8px;
}

.tools-section {
  position: relative;
  z-index: 1;
  padding: 0 40px;
  max-width: 1400px;
  margin: 0 auto;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
  gap: 20px;
}

.section-title .line {
  width: 80px;
  height: 2px;
  background: linear-gradient(90deg, transparent, #409eff, transparent);
}

.section-title .text {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.8);
  letter-spacing: 4px;
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.tool-card {
  position: relative;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.tool-card:hover {
  transform: translateY(-8px);
  border-color: rgba(64, 158, 255, 0.5);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.tool-card:hover .card-arrow {
  opacity: 1;
  transform: translateX(0);
}

.card-content {
  position: relative;
  z-index: 1;
}

.card-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 16px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 8px;
}

.card-desc {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  margin: 0 0 16px;
  line-height: 1.6;
}

.card-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  padding: 4px 10px;
  background: rgba(64, 158, 255, 0.2);
  border: 1px solid rgba(64, 158, 255, 0.3);
  border-radius: 20px;
  font-size: 12px;
  color: #409eff;
}

.card-arrow {
  position: absolute;
  right: 24px;
  top: 50%;
  transform: translate(10px, -50%);
  opacity: 0;
  transition: all 0.3s;
  color: #409eff;
  font-size: 20px;
}

.footer {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 40px;
  color: rgba(255, 255, 255, 0.4);
  font-size: 14px;
}

@media (max-width: 768px) {
  .header {
    padding: 40px 20px;
  }

  .title-text {
    font-size: 32px;
  }

  .stats {
    gap: 30px;
  }

  .stat-number {
    font-size: 28px;
  }

  .tools-section {
    padding: 0 20px;
  }

  .tools-grid {
    grid-template-columns: 1fr;
  }
}
</style>