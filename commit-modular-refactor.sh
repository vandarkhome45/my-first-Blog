#!/bin/bash

echo "📦 准备提交模块化重构..."
echo ""

# 显示更改的文件
echo "📋 本次提交包含的模块："
echo "  ✅ Blog Module - 博客核心模块（Entity/Repository/Service/Controller）"
echo "  ✅ Comment Module - 评论模块（完整功能）"
echo "  ✅ Common Components - 公共组件"
echo "  ✅ Module Config - 模块配置系统"
echo ""

git status --short

echo ""
read -p "确认要提交这些更改吗？(y/n): " confirm

if [ "$confirm" = "y" ]; then
    echo ""
    echo "➕ 添加所有文件到暂存区..."
    git add .
    
    echo ""
    echo "💾 提交到本地仓库..."
    git commit -m "refactor: 重构为模块化架构设计

架构改进：
- 采用领域驱动设计（DDD），按业务模块划分代码
- 实现高内聚低耦合的模块化结构
- 便于后续功能扩展和维护

新增模块：
1. Blog Module (module/blog/)
   - BlogEntity: 实体类（新增 category、tags 字段）
   - BlogRepository: 数据访问层
   - BlogService: 业务逻辑层（支持分类、标签查询）
   - BlogController: REST API 控制器
   
2. Comment Module (module/comment/)
   - Comment: 实体类
   - CommentRepository: 数据访问层
   - CommentService: 业务逻辑层
   - CommentController: REST API 控制器
   - CommentDTO: 数据传输对象

3. 模块配置系统
   - module-config.properties: 功能开关配置
   - ModuleConfig: 配置读取类
   - ModuleInterceptor: 模块拦截器
   - WebConfig: Web 配置类

4. 公共组件
   - Result<T>: 统一响应封装
   - PageResult<T>: 分页结果封装
   - GlobalExceptionHandler: 全局异常处理

特性：
- ✅ 支持文章分类管理
- ✅ 支持标签管理（逗号分隔字符串）
- ✅ 支持按分类筛选
- ✅ 支持按标签模糊查询
- ✅ 自动时间戳（创建/更新）
- ✅ 模块化开关控制
- ✅ 完整的中文注释

测试：
- test-modules.sh: 完整的集成测试脚本

文档：
- MODULE_ARCHITECTURE.txt: 模块化架构说明文档"
    
    echo ""
    echo "🚀 推送到远程仓库..."
    git push origin main
    
    echo ""
    echo "✅ 模块化重构提交完成！"
    echo ""
    echo "📊 最近的提交历史："
    git log --oneline -5
    echo ""
    echo "🎉 下一步："
    echo "  1. 启动应用：运行 BlogApplication"
    echo "  2. 运行测试：bash test-modules.sh"
    echo "  3. 验证功能：访问 http://localhost:8081"
else
    echo "❌ 已取消提交"
fi
