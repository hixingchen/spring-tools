# spring-tools

工具箱 - 收集平时常用或以后可能用到的开发工具

## 项目结构

```
spring-tools/
├── frontend/                # Vue 3 + Vite + Element Plus
│   └── src/
│       ├── views/          # 页面
│       ├── router/         # 路由
│       ├── api/            # API 调用
│       └── utils/          # 工具函数
│
└── backend/                 # Spring Boot 3 + Java 17 + Maven
    └── src/main/java/com/springtools/
        ├── controller/    # REST 控制器
        ├── service/       # 业务逻辑
        └── util/          # 工具类
```

## 快速开始

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

访问 http://localhost:8080/api/health 验证启动成功

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:3000 打开前端页面

## 工具列表

- [x] JSON 格式化（纯前端）
- [x] 时间戳转换
- [x] Base64 编解码
- [x] 正则表达式测试
- [x] 文本对比
- [x] 图片压缩
- [x] Excel 导入导出
- [x] Linux 命令速查
- [x] Windows 命令速查

## 技术栈

- **前端**: Vue 3 + Vite + Element Plus + Vue Router + Axios
- **后端**: Spring Boot 3.2 + Java 17 + Maven
- **许可证**: Apache License 2.0