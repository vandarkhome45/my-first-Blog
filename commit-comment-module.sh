#!/bin/bash

echo "📦 开始提交评论系统模块..."
echo ""

# 检查文件是否已修改
git status --short

echo ""
read -p "确认要提交这些文件吗？(y/n): " confirm

if [ "$confirm" = "y" ]; then
    echo ""
    echo "➕ 添加所有更改..."
    git add .
    
    echo ""
    echo "💾 提交到本地仓库..."
    git commit -m "feat: 添加模块化评论系统

核心功能：
- Comment 实体类（支持回复功能）
- CommentRepository 数据访问层
- CommentService 业务逻辑层  
- CommentController RESTful API

模块化特性：
- module-config.properties 功能开关配置
- ModuleConfig 配置读取类
- ModuleInterceptor 模块拦截器
- WebConfig 拦截器注册

API 接口：
- POST /comment - 新增评论
- GET /comment/list - 查询博客评论
- GET /comment/{id} - 查询单条评论
- DELETE /comment/{id} - 删除评论
- POST /comment/reply - 回复评论
- GET /comment/replies - 查看回复列表

测试文件：
- comment-api-test.http HTTP 接口测试

使用说明：
- 修改 module-config.properties 可启用/禁用评论系统
- 设置 module.comment.enabled=false 禁用评论功能"
    
    echo ""
    echo "🚀 推送到远程仓库..."
    git push origin main
    
    echo ""
    echo "✅ 评论系统模块提交完成！"
    echo ""
    git log --oneline -5
else
    echo "❌ 已取消提交"
fi
