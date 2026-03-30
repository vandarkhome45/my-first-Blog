#!/bin/bash

# 设置 Git 远程仓库地址脚本
# 远程仓库路径：/Users/gao_apple/Desktop/blog-system

echo "🔧 开始配置 Git 远程仓库..."
echo ""

# 检查目标目录是否存在
if [ ! -d "/Users/gao_apple/Desktop/blog-system" ]; then
    echo "❌ 错误：目录 /Users/gao_apple/Desktop/blog-system 不存在"
    echo "正在创建该目录..."
    mkdir -p /Users/gao_apple/Desktop/blog-system
    echo "✅ 目录已创建"
    echo ""
fi

# 步骤 1: 初始化当前项目的 Git 仓库（如果还没有）
echo "📦 步骤 1: 检查并初始化 Git 仓库"
if [ ! -d ".git" ]; then
    git init
    git branch -m main
    echo "✅ Git 仓库已初始化"
else
    echo "✅ Git 仓库已存在"
fi
echo ""

# 步骤 2: 添加所有文件并提交
echo "➕ 步骤 2: 添加所有文件"
git add .
echo "✅ 文件已添加"
echo ""

echo "💾 步骤 3: 提交到本地仓库"
git commit -m "初始提交：博客系统基础框架"
echo "✅ 提交完成"
echo ""

# 步骤 3: 在目标位置创建裸仓库（作为远程仓库）
echo "🏛️ 步骤 4: 在桌面创建远程裸仓库"
cd /Users/gao_apple/Desktop/blog-system
if [ ! -d "blog.git" ]; then
    git init --bare blog.git
    echo "✅ 远程裸仓库已创建：/Users/gao_apple/Desktop/blog-system/blog.git"
else
    echo "✅ 远程裸仓库已存在"
fi
echo ""

# 步骤 4: 回到项目目录并添加远程仓库
echo "🔗 步骤 5: 添加远程仓库地址"
cd /Users/gao_apple/Downloads/blog
git remote add origin /Users/gao_apple/Desktop/blog-system/blog.git
echo "✅ 远程仓库已添加"
echo ""

# 步骤 5: 推送到远程仓库
echo "🚀 步骤 6: 推送到远程仓库"
git push -u origin main
echo "✅ 推送完成"
echo ""

# 显示配置信息
echo "📊 Git 配置信息："
echo "-------------------"
git remote -v
echo "-------------------"
echo ""

echo "🎉 配置完成！"
echo ""
echo "仓库位置："
echo "  📁 本地仓库：/Users/gao_apple/Downloads/blog"
echo "  🏛️ 远程仓库：/Users/gao_apple/Desktop/blog-system/blog.git"
echo ""
echo "后续操作："
echo "  - 查看远程仓库：git remote -v"
echo "  - 推送更改：git push"
echo "  - 拉取更改：git pull"
