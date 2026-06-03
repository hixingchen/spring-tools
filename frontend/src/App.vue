<template>
  <el-container class="app-container">
    <el-aside width="200px">
      <el-menu
        :default-active="route.path"
        router
        class="side-menu"
      >
        <el-menu-item index="/">
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/json-formatter">
          <span>JSON 格式化</span>
        </el-menu-item>
        <el-menu-item index="/base64-tool">
          <span>Base64 编解码</span>
        </el-menu-item>
        <el-menu-item index="/timestamp-converter">
          <span>时间戳转换</span>
        </el-menu-item>
        <el-menu-item index="/text-diff">
          <span>文本对比</span>
        </el-menu-item>
        <el-menu-item index="/image-compressor">
          <span>图片压缩</span>
        </el-menu-item>
        <el-menu-item index="/regex-tester">
          <span>正则测试</span>
        </el-menu-item>
        <el-menu-item index="/excel-tool">
          <span>Excel 导入导出</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()
</script>

<style>
body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}

.app-container {
  height: 100vh;
}

.side-menu {
  height: 100%;
  border-right: solid 1px var(--el-border-color-light);
}
</style>
