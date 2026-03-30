#!/bin/bash

echo "=========================================="
echo "🚀 Git 仓库完整初始化脚本"
echo "=========================================="
echo ""

# 检查是否已存在 .git 目录
if [ -d ".git" ]; then
    echo "⚠️  警告：检测到已存在的 .git 目录"
    read -p "是否删除并重新开始？(y/n): " confirm
    if [ "$confirm" = "y" ]; then
        rm -rf .git
        echo "✅ 已删除旧的 .git 目录"
    else
        echo "❌ 已取消操作"
        exit 1
    fi
fi

echo ""
echo "📦 步骤 1/7: 初始化 Git 仓库"
git init
echo "✅ Git 仓库初始化完成"
echo ""

echo "🔄 步骤 2/7: 重命名分支为 main"
git branch -m main
echo "✅ 分支已重命名"
echo ""

echo "📋 步骤 3/7: 查看当前文件状态"
git status --short
echo ""

echo "➕ 步骤 4/7: 添加所有文件到暂存区"
git add .
echo "✅ 文件已添加"
echo ""

echo "💾 步骤 5/7: 提交到本地仓库"
git commit -m "feat: 初始提交 - 博客系统基础框架

- 实体类：Blog
- 数据层：BlogRepository  
- 服务层：BlogService
- 控制器：BlogController
- 通用类：Result, PageResult
- 异常处理：BlogException, GlobalExceptionHandler
- 测试用例：单元测试和 HTTP 接口测试
- 配置文件：application.properties, pom.xml"
echo "✅ 提交完成"
echo ""

echo "🏛️ 步骤 6/7: 创建远程裸仓库"
# 确保目标目录存在
mkdir -p /Users/gao_apple/Desktop/blog-system

# 如果已存在旧的仓库，先删除
if [ -d "/Users/gao_apple/Desktop/blog-system/blog.git" ]; then
    rm -rf /Users/gao_apple/Desktop/blog-system/blog.git
    echo "✅ 已清理旧的远程仓库"
fi

# 创建新的裸仓库
cd /Users/gao_apple/Desktop/blog-system
git init --bare blog.git
echo "✅ 远程裸仓库已创建：/Users/gao_apple/Desktop/blog-system/blog.git"
echo ""

echo "🔗 步骤 7/7: 关联远程仓库并推送"
cd /Users/gao_apple/Downloads/blog
git remote add origin /Users/gao_apple/Desktop/blog-system/blog.git
git push -u origin main
echo "✅ 远程仓库关联完成"
echo ""

echo "=========================================="
echo "🎉 初始化完成！"
echo "=========================================="
echo ""
echo "📊 仓库信息："
echo "  📁 本地仓库：/Users/gao_apple/Downloads/blog"
echo "  🏛️ 远程仓库：/Users/gao_apple/Desktop/blog-system/blog.git"
echo ""
echo "📋 提交历史："
git log --oneline
echo ""
echo "🔗 远程地址："
git remote -v
echo ""
echo "=========================================="
echo "✅ 一切就绪，可以开始开发了！"
echo "=========================================="
