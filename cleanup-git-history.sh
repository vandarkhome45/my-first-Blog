#!/bin/bash

# 清理重复提交历史脚本

echo "⚠️  警告：此操作将重置提交历史，只保留最新的一次提交"
echo ""
read -p "确定要继续吗？(y/n): " confirm

if [ "$confirm" = "y" ]; then
    echo "🔄 正在重置提交历史..."
    
    # 创建新分支
    git branch -m main backup-main
    
    # 基于当前代码创建新的干净提交
    git checkout --orphan temp-main
    git reset --hard
    git add .
    git commit -m "初始提交：博客系统基础框架"
    
    # 删除旧分支，重命名
    git branch -D main
    git branch -m temp-main main
    
    # 强制推送到远程
    git push -u origin main --force
    
    echo "✅ 提交历史已清理完成！"
    echo ""
    git log --oneline
else
    echo "❌ 已取消操作"
fi
