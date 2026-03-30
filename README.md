# Blog System

基于 Spring Boot 的个人博客系统，采用领域驱动设计（DDD）模块化架构。

## 技术栈

- **Java 25** + **Spring Boot 3.5.12**
- **MySQL** + Spring Data JPA
- **Spring Security** + JWT 认证
- **CommonMark** Markdown 渲染（支持 GFM 表格和自动链接）

## 功能模块

| 模块 | 说明 |
|------|------|
| Blog | 博客文章 CRUD，按分类/标签筛选，分页查询 |
| Comment | 评论与回复，支持嵌套评论 |
| Search | 全文搜索、高级搜索、关键词高亮、搜索建议 |
| Markdown | Markdown 渲染为 HTML，代码高亮，摘要提取 |
| User | 用户注册/登录、JWT 认证、资料管理、密码修改 |
| Upload | 图片/文档/文件上传，支持批量上传 |
| Archive | 按年月归档，时间线展示，最新/随机推荐 |
| Statistics | 阅读量计数，点赞/取消点赞，热门排行 |

## 快速启动

### 1. 环境要求

- JDK 25
- MySQL 8.0+
- Maven 3.9+

### 2. 创建数据库

```sql
CREATE DATABASE blog_db DEFAULT CHARACTER SET utf8mb4;
```

### 3. 配置敏感信息

```bash
cp src/main/resources/application-secret.example.properties \
   src/main/resources/application-secret.properties
```

编辑 `application-secret.properties`，填入你的数据库密码和 JWT 密钥。

### 4. 启动

```bash
./mvnw spring-boot:run
```

服务启动后访问 `http://localhost:8081`。

## 项目结构

```
src/main/java/com/example/blog/
├── module/
│   ├── blog/           # 博客核心模块
│   ├── comment/        # 评论模块
│   ├── search/         # 搜索模块
│   ├── markdown/       # Markdown 渲染模块
│   ├── user/           # 用户认证模块
│   ├── upload/         # 文件上传模块
│   ├── archive/        # 归档模块
│   └── statistics/     # 访问统计模块
├── common/             # 公共组件（Result, PageResult）
├── config/             # 全局配置（Security, ModuleConfig）
├── interceptor/        # 模块拦截器
└── exception/          # 全局异常处理
```

## API 概览

### 博客

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/blog` | 查询所有博客 | 否 |
| GET | `/blog/{id}` | 查询单个博客 | 否 |
| GET | `/blog/page?page=0&size=10` | 分页查询 | 否 |
| GET | `/blog/category?category=Java` | 按分类查询 | 否 |
| GET | `/blog/tag?tag=Spring` | 按标签查询 | 否 |
| POST | `/blog` | 新增博客 | **是** |
| PUT | `/blog/{id}` | 更新博客 | **是** |
| DELETE | `/blog/{id}` | 删除博客 | **是** |

### 评论

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/comment/list?blogId=1` | 查询博客评论 | 否 |
| GET | `/comment/{id}` | 查询单条评论 | 否 |
| GET | `/comment/replies?parentId=1` | 查看回复列表 | 否 |
| POST | `/comment?blogId=1` | 新增评论 | **是** |
| DELETE | `/comment/{id}` | 删除评论 | **是** |
| POST | `/comment/reply?parentId=1` | 回复评论 | **是** |

### 用户

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/user/register` | 用户注册 | 否 |
| POST | `/user/login` | 用户登录 | 否 |
| GET | `/user/me` | 获取当前用户 | 否* |
| GET | `/user/{id}` | 获取用户公开信息 | 否 |

*需在 Header 中携带 `Authorization: Bearer <token>`

### 其他模块

详细 API 文档见 `src/test/resources/` 下的 `.http` 测试文件。

## 安全说明

- 所有 GET 请求公开访问（只读内容）
- POST/PUT/DELETE 写操作需要携带 JWT Token
- 敏感配置（数据库密码、JWT 密钥）已从主配置文件分离，使用 `application-secret.properties` 管理
- 注册后使用 `/user/login` 获取 Token

## 许可证

MIT
