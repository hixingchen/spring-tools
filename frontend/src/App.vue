<template>
  <el-container class="app-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo">
        <span v-show="!isCollapse" class="logo-text">工具箱</span>
      </div>

      <el-menu
        :default-active="route.path"
        router
        :collapse="isCollapse"
        class="side-menu"
      >
        <template v-for="item in menus" :key="item.path">
          <el-menu-item :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.title }}</template>
          </el-menu-item>
        </template>
      </el-menu>

      <div class="collapse-btn" @click="isCollapse = !isCollapse">
        <el-icon>
          <Fold v-if="!isCollapse" />
          <Expand v-else />
        </el-icon>
      </div>
    </el-aside>

    <el-main class="main-content">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import {
  HomeFilled, Document, Key, Clock, DocumentCopy,
  Picture, Search, Files, Monitor, Platform, Fold, Expand
} from '@element-plus/icons-vue'

const route = useRoute()
const isCollapse = ref(false)

const menus = [
  { path: '/', title: '首页', icon: HomeFilled },
  { path: '/json-formatter', title: 'JSON 格式化', icon: Document },
  { path: '/base64-tool', title: 'Base64 编解码', icon: Key },
  { path: '/timestamp-converter', title: '时间戳转换', icon: Clock },
  { path: '/text-diff', title: '文本对比', icon: DocumentCopy },
  { path: '/image-compressor', title: '图片压缩', icon: Picture },
  { path: '/regex-tester', title: '正则测试', icon: Search },
  { path: '/excel-tool', title: 'Excel 导入导出', icon: Files },
  { path: '/linux-commands', title: 'Linux 命令', icon: Monitor },
  { path: '/windows-commands', title: 'Windows 命令', icon: Platform }
]
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  background-color: #0c0c1d;
}

.app-container {
  height: 100vh;
}

.aside {
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  transition: width 0.3s;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
}

.side-menu {
  flex: 1;
  border-right: none;
  background: transparent;
  overflow-y: auto;
}

.side-menu::-webkit-scrollbar {
  width: 0;
}

.side-menu .el-menu-item {
  color: rgba(255, 255, 255, 0.7);
  height: 50px;
  line-height: 50px;
  margin: 4px 8px;
  border-radius: 8px;
}

.side-menu .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.side-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
}

.side-menu .el-menu-item .el-icon {
  font-size: 18px;
  margin-right: 10px;
}

.collapse-btn {
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.6);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s;
}

.collapse-btn:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.1);
}

.main-content {
  background: transparent;
  padding: 0;
  overflow-y: auto;
}
</style>