import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/json-formatter',
    name: 'JsonFormatter',
    component: () => import('../views/JsonFormatter.vue')
  },
  {
    path: '/base64-tool',
    name: 'Base64Tool',
    component: () => import('../views/Base64Tool.vue')
  },
  {
    path: '/timestamp-converter',
    name: 'TimestampConverter',
    component: () => import('../views/TimestampConverter.vue')
  },
  {
    path: '/text-diff',
    name: 'TextDiff',
    component: () => import('../views/TextDiff.vue')
  },
  {
    path: '/image-compressor',
    name: 'ImageCompressor',
    component: () => import('../views/ImageCompressor.vue')
  },
  {
    path: '/regex-tester',
    name: 'RegexTester',
    component: () => import('../views/RegexTester.vue')
  },
  {
    path: '/excel-tool',
    name: 'ExcelTool',
    component: () => import('../views/ExcelTool.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
