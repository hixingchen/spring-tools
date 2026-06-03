<template>
  <div class="page-header">
    <div class="header-content">
      <div class="header-left">
        <div class="icon-wrapper">
          <el-icon><component :is="icon" /></el-icon>
        </div>
        <div class="header-text">
          <h1 class="page-title">{{ title }}</h1>
          <p class="page-desc">{{ description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  icon: {
    type: Object,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  }
})
</script>

<style scoped>
.page-header {
  background: linear-gradient(135deg, #2c3e5a 0%, #1d2b40 100%);
  padding: 28px 40px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.header-content {
  display: flex;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.icon-wrapper {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 22px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.header-text {
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
  padding: 0;
  line-height: 1.4;
  color: #e8edf3;
}

.page-desc {
  margin: 0;
  padding: 0;
  font-size: 13px;
  line-height: 1.4;
  color: rgba(255, 255, 255, 0.55);
}
</style>
