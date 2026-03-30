#!/bin/bash

# Git 仓库初始化脚本
# 使用方法：在终端运行 bash git-init-steps.sh

echo "🚀 开始初始化 Git 仓库..."
echo ""

# 步骤 1: 初始化 Git 仓库
echo "📦 步骤 1: 初始化 Git 仓库"
git init
echo "✅ Git 仓库初始化完成"
echo ""

# 步骤 2: 重命名分支为 main
echo "🔄 步骤 2: 重命名分支为 main"
git branch -m main
echo "✅ 分支已重命名为 main"
echo ""

# 步骤 3: 查看当前状态
echo "📋 步骤 3: 查看当前文件状态"
git status
echo ""

# 步骤 4: 添加所有文件
echo "➕ 步骤 4: 添加所有文件到暂存区"
git add .
echo "✅ 文件已添加到暂存区"
echo ""

# 步骤 5: 再次查看状态
echo "📋 步骤 5: 确认文件状态"
git status
echo ""

# 步骤 6: 提交到仓库
echo "💾 步骤 6: 提交到本地仓库"
git commit -m "初始提交：博客系统基础框架"
echo "✅ 提交完成"
echo ""

# 步骤 7: 查看提交历史
echo "📜 步骤 7: 查看提交历史"
git log --oneline
echo ""

echo "🎉 Git 仓库初始化完成！"
echo ""
echo "下一步建议："
echo "1. 创建 GitHub/Gitee 远程仓库"
echo "2. 关联远程仓库：git remote add origin <仓库地址>"
echo "3. 推送到远程：git push -u origin main"
