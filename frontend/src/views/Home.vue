<template>
  <div class="home">
    <h2>工具列表</h2>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="tool-card" @click="router.push('/json-formatter')">
          <h3>JSON 格式化</h3>
          <p>格式化和校验 JSON 数据</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="tool-card" @click="router.push('/base64-tool')">
          <h3>Base64 编解码</h3>
          <p>Base64 文本编码和解码</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="tool-card" @click="router.push('/timestamp-converter')">
          <h3>时间戳转换</h3>
          <p>时间戳与日期互相转换</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="tool-card" @click="router.push('/text-diff')">
          <h3>文本对比</h3>
          <p>对比两段文本的差异</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="tool-card" @click="router.push('/image-compressor')">
          <h3>图片压缩</h3>
          <p>前端 Canvas 压缩图片，支持质量调节</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="tool-card" @click="router.push('/regex-tester')">
          <h3>正则表达式测试</h3>
          <p>测试和调试正则表达式</p>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="tool-card" @click="router.push('/excel-tool')">
          <h3>Excel 导入导出</h3>
          <p>Excel 文件导入导出，支持下拉选择</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped>
.home {
  padding: 20px;
}

.tool-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.tool-card:hover {
  transform: translateY(-4px);
}

.tool-card h3 {
  margin: 0 0 8px;
}

.tool-card p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}
</style>
