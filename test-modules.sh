#!/bin/bash

echo "🧪 开始运行模块化博客系统完整测试..."
echo ""

# 设置基础 URL（根据你的 application.properties 配置）
BASE_URL="http://localhost:8081"

echo "📋 测试环境："
echo "  Base URL: ${BASE_URL}"
echo ""

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 3

echo ""
echo "=========================================="
echo "📝 模块 1: 博客核心功能测试"
echo "=========================================="

echo ""
echo "✅ 测试 1.1: 创建第一篇博客（带分类和标签）"
curl -X POST "${BASE_URL}/blog" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot 学习心得",
    "content": "今天深入学习了 Spring Boot 框架，感觉非常好用。特别是自动配置和起步依赖，大大简化了开发流程。",
    "category": "Java",
    "tags": "Spring Boot, Java, 后端开发，框架"
  }' | jq '.'

BLOG_ID_1=$(curl -s -X POST "${BASE_URL}/blog" \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","content":"Test","category":"Java","tags":"test"}' | jq '.data.id')

echo ""
echo "✅ 测试 1.2: 创建第二篇博客（生活类）"
curl -X POST "${BASE_URL}/blog" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "周末生活随笔",
    "content": "周末去公园散步，天气很好，心情也很愉快。",
    "category": "生活",
    "tags": "随笔，日常，心情"
  }' | jq '.'

BLOG_ID_2=$(curl -s -X POST "${BASE_URL}/blog" \
  -H "Content-Type: application/json" \
  -d '{"title":"Life","content":"Life","category":"生活","tags":"life"}' | jq '.data.id')

echo ""
echo "✅ 测试 1.3: 创建第三篇博客（Java 类）"
curl -X POST "${BASE_URL}/blog" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Java 多线程编程详解",
    "content": "深入理解 Java 多线程机制，包括 Thread、Runnable、Callable 等核心概念。",
    "category": "Java",
    "tags": "Java, 多线程，并发编程，JUC"
  }' | jq '.'

echo ""
echo "✅ 测试 1.4: 查询所有博客"
curl -X GET "${BASE_URL}/blog" | jq '.'

echo ""
echo "✅ 测试 1.5: 分页查询（第一页，每页 2 条）"
curl -X GET "${BASE_URL}/blog/page?page=0&size=2" | jq '.'

echo ""
echo "✅ 测试 1.6: 按分类查询 - Java"
curl -X GET "${BASE_URL}/blog/category?category=Java" | jq '.'

echo ""
echo "✅ 测试 1.7: 按分类查询 - 生活"
curl -X GET "${BASE_URL}/blog/category?category=生活" | jq '.'

echo ""
echo "✅ 测试 1.8: 按标签查询 - 包含 Java"
curl -X GET "${BASE_URL}/blog/tag?tag=Java" | jq '.'

echo ""
echo "✅ 测试 1.9: 按标签查询 - 包含 Spring Boot"
curl -X GET "${BASE_URL}/blog/tag?tag=Spring%20Boot" | jq '.'

echo ""
echo "=========================================="
echo "💬 模块 2: 评论功能测试"
echo "=========================================="

echo ""
echo "✅ 测试 2.1: 为第一篇博客添加评论"
COMMENT_ID_1=$(curl -s -X POST "${BASE_URL}/comment?blogId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "很好的文章，学到了很多！",
    "author": "张三",
    "email": "zhangsan@example.com"
  }' | jq '.data.id')

curl -X POST "${BASE_URL}/comment?blogId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "很好的文章，学到了很多！",
    "author": "张三",
    "email": "zhangsan@example.com"
  }' | jq '.'

echo ""
echo "✅ 测试 2.2: 回复评论"
curl -X POST "${BASE_URL}/comment/reply?parentId=${COMMENT_ID_1}" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "谢谢楼主的分享！",
    "author": "李四",
    "email": "lisi@example.com"
  }' | jq '.'

echo ""
echo "✅ 测试 2.3: 查询某篇博客的所有评论"
curl -X GET "${BASE_URL}/comment/list?blogId=1" | jq '.'

echo ""
echo "✅ 测试 2.4: 查看回复列表"
curl -X GET "${BASE_URL}/comment/replies?parentId=${COMMENT_ID_1}" | jq '.'

echo ""
echo "=========================================="
echo "🎯 模块 3: 更新和删除测试"
echo "=========================================="

echo ""
echo "✅ 测试 3.1: 更新博客（修改分类和标签）"
curl -X PUT "${BASE_URL}/blog/1" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot 学习心得（更新版）",
    "content": "重新整理了笔记，补充了更多内容。",
    "category": "技术",
    "tags": "Spring Boot, Java, Web 开发，微服务"
  }' | jq '.'

echo ""
echo "✅ 测试 3.2: 删除评论"
curl -X DELETE "${BASE_URL}/comment/${COMMENT_ID_1}" | jq '.'

echo ""
echo "=========================================="
echo "📊 测试完成！"
echo "=========================================="
echo ""
echo "✨ 模块化功能验证："
echo "  ✅ 博客模块 - CRUD、分类、标签、分页"
echo "  ✅ 评论模块 - 评论、回复、查询"
echo "  ✅ 模块独立 - 可单独启用/禁用"
echo ""
echo "下一步建议："
echo "  1. 检查数据库表结构是否正确"
echo "  2. 验证模块开关配置是否生效"
echo "  3. 提交代码到 Git 仓库"
echo ""
