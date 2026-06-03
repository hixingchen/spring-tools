# Craft — 项目工程规范

> 本文件是项目最高优先级指令，所有代码变更必须严格遵守。

---

## 一、架构规范

### 1.1 后端分层（自上而下，禁止反向依赖）

```
controller  → 只做参数接收、校验、调用 service、返回 Result
service     → 业务逻辑层，事务控制在此层
mapper      → 数据访问层，继承 BaseMapper，复杂 SQL 写 XML
entity      → 数据库实体，一个表一个类，字段与表严格对应
```

### 1.2 Maven 多模块结构（平级，大厂标准）

```
backend/
  pom.xml                        ← 父 POM（聚合 + 版本管理）
  craft-common/           ← 公共模块
  craft-excel/            ← Excel 模块
  craft-system/           ← 系统模块
  craft-tool/             ← 工具模块
  craft-user/             ← 用户模块
  craft-admin/            ← 启动模块（Application + 配置 + mapper XML）
```

### 1.3 依赖关系（单向，禁止反向）

```
admin → common, excel, system, tool, user（聚合所有模块）
excel/system/tool/user → common（只依赖公共模块）
模块之间禁止互相依赖，需要共享逻辑下沉到 common
```

### 1.3 模块边界（铁律）

- **模块之间禁止互相 import**，需要共享的逻辑必须下沉到 `common/`
- 每个模块内部四层结构：`controller → service → mapper → entity`
- Controller 类只放在自己模块的 `controller/` 包下
- 跨模块调用通过 service 接口，不直接调 mapper

---

## 二、编码规范

### 2.1 Java 命名

| 类型 | 规则 | 示例 |
|------|------|------|
| 包名 | 全小写，模块名.层级 | `com.craft.module.tool.controller` |
| 类名 | UpperCamelCase | `ToolController`、`UserService` |
| 方法名 | lowerCamelCase | `getToolById()`、`createUser()` |
| 常量 | UPPER_SNAKE_CASE | `MAX_PAGE_SIZE` |
| 变量 | lowerCamelCase | `toolName`、`createTime` |
| 表名 | snake_case，复数 | `tools`、`users`、`tool_categories` |
| 字段名 | snake_case | `create_time`、`is_deleted` |

### 2.2 Controller 命名

```
类名：XxxController
路径：/api/模块名/资源
方法前缀：
  查询 → get / list
  新增 → create / add
  修改 → update
  删除 → delete / remove
```

### 2.3 Service 命名

```
接口：XxxService
实现：XxxServiceImpl
方法前缀：同 Controller，保持一致
```

### 2.4 禁止事项

- 禁止在 Controller 里写业务逻辑
- 禁止在 Service 里直接操作 HttpServletResponse
- 禁止用 `System.out.println`，统一用 `@Slf4j` 的 `log`
- 禁止用 `e.printStackTrace()`，用 `log.error("描述", e)`
- 禁止魔法值，必须定义常量或枚举
- 禁止 `SELECT *`，明确查询字段
- 禁止在循环中调用数据库，用批量操作
- 禁止 catch 后不处理（吞异常）

---

## 三、接口规范

### 3.1 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

所有接口必须返回 `Result<T>`，不允许直接返回裸对象。

### 3.2 RESTful 路径设计

```
GET    /api/tool/list          → 列表查询
GET    /api/tool/{id}          → 详情
POST   /api/tool               → 新增
PUT    /api/tool               → 修改
DELETE /api/tool/{id}          → 删除
```

### 3.3 参数校验

- 入参用 `@Valid` + JSR 303 注解（`@NotBlank`、`@Size`、`@Email` 等）
- 校验失败由 `GlobalExceptionHandler` 统一捕获，返回 400
- 业务校验（如数据不存在）在 Service 层抛 `BusinessException`

### 3.4 状态码使用

```
200  → 成功
400  → 参数错误
401  → 未登录
403  → 无权限
500  → 服务端异常
自定义业务码 → 见 ResultCode 枚举
```

---

## 四、数据库规范

### 4.1 表设计

- 每张表必须有 `id`（主键，自增或雪花）、`create_time`、`update_time`
- 软删除字段统一用 `deleted`（0=未删除，1=已删除）
- 字段类型：时间用 `datetime`，金额用 `decimal`，状态用 `tinyint`
- 索引命名：`idx_表名_字段名`，唯一索引：`uk_表名_字段名`

### 4.2 MyBatis-Plus 使用

- 单表 CRUD 继承 `BaseMapper<T>`，零 XML
- 复杂查询写 XML，放在 `resources/mapper/模块名/` 下
- 分页查询必须用 `Page<T>` + `PaginationInnerInterceptor`
- 逻辑删除配置在 `application.yml` 全局生效，实体加 `@TableLogic`

### 4.3 SQL 禁止

- 禁止 `DELETE` 物理删除（用逻辑删除）
- 禁止不带 `WHERE` 条件的 `UPDATE` / `DELETE`
- 禁止在代码里拼接 SQL 字符串

---

## 五、前端规范

### 5.1 目录结构

```
src/
  api/           → 接口层，按模块分文件（user.js、tool.js）
  api/request.js → axios 统一封装，不要直接用 axios
  stores/        → Pinia 状态管理，按模块分文件
  components/    → 公共组件，必须有明确复用场景才放这里
  views/         → 页面，每个工具一个文件
  router/        → 路由配置，工具页必须懒加载
```

### 5.2 接口调用

- 统一使用 `api/request.js` 导出的 `request` 实例
- 接口定义放在 `api/` 对应模块文件中，页面不直接写 axios
- 文件下载用 `downloadFile()` 方法
- 错误由拦截器统一弹窗，页面不需要重复 `catch`

### 5.3 组件规范

- 工具页头部统一使用 `PageHeader` 组件
- 页面不直接操作 DOM，用 Vue 响应式
- 组件 props 必须声明类型和是否必填
- 组件内 `scoped` 样式，不污染全局

### 5.4 路由规范

```javascript
// 正确：懒加载
{ path: '/xxx', component: () => import('../views/Xxx.vue') }

// 错误：同步导入（仅首页允许）
import Xxx from '../views/Xxx.vue'
```

---

## 六、Git 规范

### 6.1 分支策略

```
main        → 生产分支，保护分支，只接受 PR
dev         → 开发分支，日常提交
feature/*   → 功能分支，从 dev 切出
hotfix/*    → 紧急修复，从 main 切出
```

### 6.2 Commit Message 格式

```
<type>(<scope>): <subject>

type:
  feat     → 新功能
  fix      → 修复
  refactor → 重构
  style    → 样式调整
  docs     → 文档
  test     → 测试
  chore    → 构建/工具变更

scope: module 名（tool、user、common、frontend）

示例：
  feat(tool): 新增 JSON 格式化工具
  fix(user): 修复登录 Token 过期未清除问题
  refactor(common): 统一异常处理抽取到 GlobalExceptionHandler
  style(frontend): 优化 PageHeader 组件间距
```

### 6.3 禁止提交

- 禁止提交 `node_modules/`、`target/`、`dist/`、`.idea/`
- 禁止提交 `application-prod.yml` 中的真实密码
- 禁止一次提交涉及多个不相关功能

---

## 七、安全规范

### 7.1 接口安全

- 所有写操作（POST/PUT/DELETE）必须走 Sa-Token 鉴权
- 公开接口（如工具页展示）用 `GET`，不强制登录
- 敏感操作（删除、修改他人数据）需要权限校验
- 文件上传必须校验类型和大小

### 7.2 数据安全

- 密码存储用 BCrypt，禁止明文
- 用户输入必须校验，防止 XSS
- SQL 查询用参数化，禁止字符串拼接
- 生产配置文件不入库，敏感信息用环境变量

### 7.3 接口限流

- 登录接口限制频率（后续用 Sa-Token 的 `SaInterceptor` 或 Redis 计数器）
- 文件上传接口限制大小（当前 10MB）

---

## 八、文档规范

### 8.1 接口文档

- 所有 Controller 方法必须加 Knife4j 注解（`@Tag`、`@Operation`、`@Parameter`）
- 注解描述用中文，与前端展示一致
- 示例值必须是真实可用的数据

### 8.2 代码注释

- 类：说明职责
- 公共方法：说明参数含义和返回值
- 复杂逻辑：说明 why，不是 what
- 不注释显而易见的代码（如 `i++ // 自增`）

---

## 九、开发流程

### 9.1 新增工具的标准流程

```
后端：
  1. module/工具名/entity/     → 建实体类
  2. module/工具名/mapper/     → 建 Mapper 接口
  3. module/工具名/service/    → 建 Service 接口 + 实现
  4. module/工具名/controller/ → 建 Controller，加 Knife4j 注解
  5. resources/mapper/工具名/  → 复杂 SQL 的 XML（如需要）

前端：
  1. views/XxxTool.vue         → 页面（用 PageHeader 组件）
  2. api/xxx.js                → 接口定义
  3. router/index.js           → 注册路由（懒加载）
```

### 9.2 启动命令

```bash
# 后端（开发环境，H2 + 内存认证，零外部依赖）
cd backend && mvn spring-boot:run

# 前端
cd frontend && npm run dev
```

---

## 十、技术选型锁定

| 组件 | 选型 | 版本 | 禁止替换为 |
|------|------|------|-----------|
| ORM | MyBatis-Plus | 3.5.5 | 不用 JPA |
| 认证 | Sa-Token | 1.37.0 | 不用 Spring Security |
| 接口文档 | Knife4j | 4.5.0 | 不用原生 Swagger |
| 前端框架 | Vue 3 | 3.4 | 不用 React |
| UI 库 | Element Plus | 2.9 | 不用 Ant Design Vue |
| 状态管理 | Pinia | - | 不用 Vuex |
| HTTP | Axios | 1.7 | 不用 fetch |

技术栈变更必须团队讨论确认，个人不得擅自替换。
