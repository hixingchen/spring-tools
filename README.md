# Craft - 开发者工具箱

收集平时常用或以后可能用到的开发工具，采用前后端分离架构，大部分工具纯前端实现，无需后端即可使用。

## 项目结构

```
craft/
├── frontend/                          # Vue 3 单页应用
│   ├── vite.config.js                 # Vite 配置（代理 /api -> :8080）
│   └── src/
│       ├── views/                     # 页面组件（每个工具一个页面）
│       ├── components/                # 公共组件
│       ├── router/                    # 路由配置
│       ├── api/                       # API 请求封装
│       └── stores/                    # Pinia 状态管理
│
└── backend/                           # Spring Boot 多模块 Maven 项目
    ├── pom.xml                        # 父 POM（模块聚合）
    ├── craft-common/                  # 公共模块（统一响应、异常处理）
    ├── craft-admin/                   # 启动模块（Spring Boot 入口）
    ├── craft-system/                  # 系统管理（健康检查）
    ├── craft-excel/                   # Excel 导入导出
    ├── craft-tool/                    # 工具管理（待开发）
    └── craft-user/                    # 用户认证（待开发）
```

### 模块依赖关系

```
craft-admin（启动聚合）
  ├── craft-common（统一响应、异常处理）
  ├── craft-excel（依赖 craft-common、EasyExcel）
  ├── craft-system（依赖 craft-common）
  ├── craft-tool（依赖 craft-common、MyBatis-Plus）── 待开发
  └── craft-user（依赖 craft-common、Sa-Token）── 待开发
```

## 技术栈

| 层级 | 技术 |
|------|------|
| **前端框架** | Vue 3.4 + Vue Router 4.3 + Pinia 3.0 |
| **构建工具** | Vite 5.4 |
| **UI 组件库** | Element Plus 2.9 |
| **HTTP 客户端** | Axios 1.7 |
| **后端框架** | Spring Boot 3.2.5 + Java 17 |
| **构建工具** | Maven（多模块） |
| **ORM** | MyBatis-Plus 3.5.5 |
| **认证** | Sa-Token 1.37.0 |
| **API 文档** | Knife4j 4.5.0（OpenAPI 3） |
| **Excel 处理** | Alibaba EasyExcel 3.3.4 |
| **数据库** | MySQL + Redis |
| **许可证** | Apache License 2.0 |

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+
- Redis（可选，已配置但暂未重度使用）

### 后端启动

```bash
cd backend

# 配置数据库（编辑 craft-admin/src/main/resources/application-dev.yml）
# 默认连接 localhost:3306/craft，用户名 root，密码 123456

# 编译并启动
mvn spring-boot:run -pl craft-admin
```

- 健康检查: http://localhost:8080/api/health
- API 文档: http://localhost:8080/swagger-ui.html

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

访问 http://localhost:3000 打开前端页面

### 前端构建

```bash
cd frontend
npm run build
```

构建产物输出到 `frontend/dist/` 目录。

## 工具列表

| 工具 | 说明 | 实现方式 |
|------|------|----------|
| JSON 格式化 | 格式化、压缩、校验 JSON | 纯前端 |
| 时间戳转换 | Unix 时间戳与日期互转 | 纯前端 |
| Base64 编解码 | 文本的 Base64 编码与解码 | 纯前端 |
| 正则表达式测试 | 实时正则匹配与高亮 | 纯前端 |
| 文本对比 | 两段文本的差异对比（LCS 算法） | 纯前端 |
| 图片压缩 | Canvas 实现的图片压缩与预览 | 纯前端 |
| Excel 导入导出 | Excel 模板下载、数据导入导出 | 前端 + 后端 |
| Linux 命令速查 | 常用 Linux 命令参考手册 | 纯前端 |
| Windows 命令速查 | 常用 Windows 命令参考手册 | 纯前端 |

> 大部分工具为纯前端实现，数据不会离开浏览器。仅 Excel 导入导出需要后端服务支持。

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/health` | 健康检查 |
| GET | `/api/excel/template` | 下载 Excel 导入模板 |
| GET | `/api/excel/export` | 导出示例数据 |
| POST | `/api/excel/import` | 导入 Excel 文件 |

完整 API 文档启动后访问 http://localhost:8080/swagger-ui.html

## 配置说明

后端配置文件位于 `backend/craft-admin/src/main/resources/`：

| 文件 | 说明 |
|------|------|
| `application.yml` | 主配置（端口、CORS、MyBatis-Plus、Sa-Token） |
| `application-dev.yml` | 开发环境（MySQL、Redis 连接信息） |
| `application-prod.yml` | 生产环境（支持环境变量覆盖） |

默认开发环境端口：后端 `8080`，前端 `3000`，Vite 自动代理 `/api` 请求到后端。

## 许可证

[Apache License 2.0](LICENSE)